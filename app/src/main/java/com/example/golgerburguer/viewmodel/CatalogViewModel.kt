package com.example.golgerburguer.viewmodel




import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golgerburguer.model.ProductRepository
import com.example.golgerburguer.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale


/**
 * Data class para representar un item dentro del carrito de compras.
 * Incluye el producto y la cantidad seleccionada.
 */
data class CartItem(
    val product: Producto,
    val quantity: Int
)


/**
 * Data class que representa el estado completo de la interfaz de usuario
 * para las pantallas relacionadas con el catálogo (Home, Favoritos, Carrito).
 * @property products La lista completa de productos obtenida de la base de datos.
 * @property favorites La lista de productos marcados como favoritos, obtenida de la base de datos.
 * @property cartItems La lista de items actualmente en el carrito de compras (gestionada en memoria).
 */
data class CatalogUiState(
    val products: List<Producto> = emptyList(),
    val favorites: List<Producto> = emptyList(),
    val cartItems: List<CartItem> = emptyList()
) {
    /**
     * Propiedad calculada que devuelve el subtotal del carrito.
     * Suma el precio de cada producto multiplicado por su cantidad.
     */
    val cartSubtotal: Double
        get() = cartItems.sumOf { it.product.precio * it.quantity }
}


/**
 * ViewModel principal para gestionar los datos del catálogo, favoritos y carrito.
 * Interactúa con ProductRepository para acceder a la base de datos Room.
 * @param repository El repositorio que proporciona acceso a los datos de los productos.
 */
class CatalogViewModel(private val repository: ProductRepository) : ViewModel() {


    // _uiState: Flujo mutable y privado que contiene el estado actual de la UI.
    private val _uiState = MutableStateFlow(CatalogUiState())
    // uiState: Flujo público e inmutable que las pantallas observan para obtener actualizaciones.
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()


    init {
        // Al iniciar el ViewModel:
        // 1. Lanza una corutina en el hilo IO para verificar si la BD necesita ser poblada.
        viewModelScope.launch(Dispatchers.IO) {
            repository.checkAndPrepopulateDatabase()
        }
        // 2. Comienza a observar los Flows del repositorio para recibir actualizaciones de la BD.
        observeProducts()
        observeFavorites()
    }


    /**
     * Se suscribe al Flow 'allProducts' del repositorio.
     * Cada vez que la lista de productos en la BD cambie, actualiza el estado 'products' en _uiState.
     */
    private fun observeProducts() {
        viewModelScope.launch {
            repository.allProducts // Flow<List<Producto>> desde el DAO
                .catch { exception ->
                    // TODO: Manejar errores de la base de datos (ej. mostrar mensaje al usuario)
                    println("Error observing products: $exception")
                }
                .collect { productList ->
                    _uiState.update { currentState ->
                        currentState.copy(products = productList)
                    }
                }
        }
    }


    /**
     * Se suscribe al Flow 'favoriteProducts' del repositorio.
     * Cada vez que la lista de favoritos en la BD cambie, actualiza el estado 'favorites' en _uiState.
     */
    private fun observeFavorites() {
        viewModelScope.launch {
            repository.favoriteProducts // Flow<List<Producto>> desde el DAO (filtrados por es_favorito = 1)
                .catch { exception ->
                    // TODO: Manejar errores
                    println("Error observing favorites: $exception")
                }
                .collect { favoriteList ->
                    _uiState.update { currentState ->
                        currentState.copy(favorites = favoriteList)
                    }
                }
        }
    }


    /**
     * Cambia el estado de favorito de un producto en la base de datos.
     * Lanza una corutina en el hilo IO para realizar la operación de escritura.
     * @param productId El ID del producto a modificar.
     * @param isCurrentlyFavorite El estado de favorito actual del producto (antes del clic).
     */
    fun toggleFavorite(productId: Int, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { // Ejecuta la actualización de BD en segundo plano
            repository.updateFavorite(productId, !isCurrentlyFavorite) // Llama al repositorio
        }
    }


    // --- LÓGICA DEL CARRITO (Gestionada en memoria, no persistente en BD) ---


    /**
     * Añade un producto al carrito o incrementa su cantidad si ya existe.
     * @param product El producto a añadir.
     */
    fun addToCart(product: Producto) {
        _uiState.update { currentState ->
            val cart = currentState.cartItems.toMutableList()
            val existingItemIndex = cart.indexOfFirst { it.product.id == product.id }


            if (existingItemIndex != -1) {
                // Si el item ya existe, crea una copia con la cantidad incrementada
                val existingItem = cart[existingItemIndex]
                cart[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity + 1)
            } else {
                // Si es un item nuevo, lo añade con cantidad 1
                cart.add(CartItem(product = product, quantity = 1))
            }
            currentState.copy(cartItems = cart) // Actualiza el estado con la nueva lista del carrito
        }
    }


    /**
     * Incrementa la cantidad de un producto específico en el carrito.
     * @param productId El ID del producto cuya cantidad se incrementará.
     */
    fun increaseQuantity(productId: Int) {
        _uiState.update { currentState ->
            val updatedCart = currentState.cartItems.map { cartItem ->
                if (cartItem.product.id == productId) {
                    cartItem.copy(quantity = cartItem.quantity + 1) // Incrementa cantidad
                } else {
                    cartItem // Mantiene el item igual si no es el buscado
                }
            }
            currentState.copy(cartItems = updatedCart)
        }
    }


    /**
     * Decrementa la cantidad de un producto específico en el carrito.
     * Si la cantidad llega a 0, elimina el producto del carrito.
     * @param productId El ID del producto cuya cantidad se decrementará.
     */
    fun decreaseQuantity(productId: Int) {
        _uiState.update { currentState ->
            val cart = currentState.cartItems.toMutableList()
            val itemIndex = cart.indexOfFirst { it.product.id == productId }


            if (itemIndex != -1) {
                val item = cart[itemIndex]
                if (item.quantity > 1) {
                    // Si la cantidad es mayor a 1, la decrementa
                    cart[itemIndex] = item.copy(quantity = item.quantity - 1)
                } else {
                    // Si la cantidad es 1, elimina el item del carrito
                    cart.removeAt(itemIndex)
                }
            }
            currentState.copy(cartItems = cart) // Actualiza el estado
        }
    }
}


/**
 * Función de extensión para formatear un Double como moneda Chilena (CLP) sin decimales.
 * Ejemplo: 6990.0 -> "$6.990"
 * @return El valor formateado como String.
 */
fun Double.toCurrencyFormat(): String {
    // Obtiene una instancia de formateador de moneda para Chile
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    // Configura para no mostrar decimales
    format.maximumFractionDigits = 0
    // Formatea el número y quita el símbolo "CLP" (opcional, para limpieza)
    return format.format(this).replace("CLP", "").trim()
}




