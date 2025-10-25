package com.example.golgerburguer.model

import kotlinx.coroutines.flow.Flow

/**
 * [ACTUALIZADO] Añadida la función para actualizar un usuario.
 */
class ProductRepository(
    private val productDao: ProductDao,
    private val userDao: UserDao
) {

    // --- Operaciones de Productos ---

    val allProducts: Flow<List<Producto>> = productDao.getAllProducts()

    val favoriteProducts: Flow<List<Producto>> = productDao.getFavoriteProducts()

    suspend fun updateFavorite(productId: Int, isFavorite: Boolean) {
        productDao.updateFavorite(productId, isFavorite)
    }

    // --- Operaciones de Usuarios ---

    suspend fun registerUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun findUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    /**
     * [NUEVO] Actualiza un usuario existente a través del UserDao.
     * @param user El objeto User con los datos actualizados.
     */
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
}
