package com.example.golgerburguer.view




import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
// Eliminamos import de sp si usamos estilos del tema
// import androidx.compose.ui.unit.sp
import com.example.golgerburguer.viewmodel.CatalogViewModel


/**
 * Pantalla Composable que muestra la lista de productos marcados como favoritos.
 * @param catalogViewModel ViewModel que contiene el estado del catálogo, incluyendo los favoritos.
 */
@Composable
fun FavoritesScreen(catalogViewModel: CatalogViewModel) { // Recibe el ViewModel
    // Observamos el estado del ViewModel para obtener la lista de favoritos.
    val uiState by catalogViewModel.uiState.collectAsState()


    // Usamos la lista de favoritos que el ViewModel ya preparó desde la BD.
    val favoriteProducts = uiState.favorites


    Surface(
        modifier = Modifier.fillMaxSize(),
        // Usamos el color de fondo definido en el tema.
        color = MaterialTheme.colorScheme.background
    ) {
        // Si la lista de favoritos está vacía, muestra un mensaje.
        if (favoriteProducts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Centra el contenido
            ) {
                Text(
                    text = "Aún no has agregado productos a tus favoritos.\n¡Marca algunos con un corazón!",
                    // Aplica estilo y color del tema para el mensaje.
                    style = MaterialTheme.typography.bodyLarge, // Estilo de cuerpo de texto
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Color para texto secundario/informativo
                    textAlign = TextAlign.Center, // Centra el texto
                    modifier = Modifier.padding(32.dp) // Añade padding alrededor
                )
            }
        } else {
            // Si hay favoritos, muestra la lista usando LazyColumn.
            LazyColumn(
                contentPadding = PaddingValues(16.dp), // Espaciado interno
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre tarjetas
            ) {
                // Itera sobre la lista de productos favoritos.
                items(favoriteProducts, key = { it.id }) { product -> // Usa el id como key para optimizar
                    // Reutiliza el Composable ProductCard.
                    ProductCard(
                        product = product,
                        // Pasa la acción para cambiar el estado de favorito.
                        onFavoriteClick = {
                            catalogViewModel.toggleFavorite(product.id, product.esFavorito)
                        },
                        // Pasa la acción para añadir al carrito.
                        onAddToCartClick = { catalogViewModel.addToCart(product) }
                    )
                }
            }
        }
    }
}






