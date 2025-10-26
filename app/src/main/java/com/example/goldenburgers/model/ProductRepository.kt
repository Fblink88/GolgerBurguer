package com.example.goldenburgers.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
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

/**
 * [ACTUALIZADO] Se añade el campo para la URI de la imagen de perfil.
 */
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val password: String,
    val fullName: String,
    val phoneNumber: String,
    val gender: String,
    val birthDate: String,
    val street: String,
    val number: String,
    val city: String,
    val region: String,
    val commune: String,
    // [NUEVO] Campo para almacenar la ruta (URI) de la foto de perfil.
    val profileImageUri: String? = null
)
