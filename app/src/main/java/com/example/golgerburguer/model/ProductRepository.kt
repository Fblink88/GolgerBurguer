package com.example.golgerburguer.model// Asegúrate que el paquete coincida


import com.example.golgerburguer.data.ProductDao
import kotlinx.coroutines.flow.Flow


/**
 * El Repositorio gestiona las operaciones de datos para los Productos.
 * Abstrae la fuente de datos (en este caso, el ProductDao) del resto de la aplicación,
 * especialmente del ViewModel.
 */
class ProductRepository(private val productDao: ProductDao) {


    /**
     * Expone un Flow con la lista completa de productos obtenidos del DAO.
     * El ViewModel observará este Flow para obtener las actualizaciones en tiempo real.
     */
    val allProducts: Flow<List<Producto>> = productDao.getAllProducts()


    /**
     * Expone un Flow con la lista de productos marcados como favoritos obtenidos del DAO.
     * El ViewModel observará este Flow.
     */
    val favoriteProducts: Flow<List<Producto>> = productDao.getFavoriteProducts()


    /**
     * Llama al DAO para actualizar el estado de favorito de un producto específico.
     * Esta función es 'suspend' porque la operación del DAO lo es (accede a la BD).
     * @param productId El ID del producto a actualizar.
     * @param isFavorite El nuevo estado de favorito a guardar.
     */
    suspend fun updateFavorite(productId: Int, isFavorite: Boolean) {
        productDao.updateFavorite(productId, isFavorite)
    }


    /**
     * Verifica si la base de datos está vacía llamando a getProductCount() en el DAO.
     * Si está vacía (conteo es 0), la puebla con los datos iniciales de FakeProductDataSource.
     * Esta función es 'suspend' porque las operaciones del DAO (getProductCount, insertAll) lo son.
     * Se llama desde el ViewModel al iniciar la app.
     */
    suspend fun checkAndPrepopulateDatabase() {
        if (productDao.getProductCount() == 0) {
            // Inserta la lista de productos definida en FakeProductDataSource
            productDao.insertAll(FakeProductDataSource.products)
        }
    }
}





