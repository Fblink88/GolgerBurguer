package com.example.golgerburguer.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update // <-- AÑADIDO

/**
 * [ACTUALIZADO] Añadida la función para actualizar un usuario.
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    /**
     * [NUEVO] Actualiza un usuario existente en la base de datos.
     * Room usa la clave primaria (el 'id' del usuario) para encontrar la fila a actualizar.
     * @param user El objeto User con los datos actualizados.
     */
    @Update
    suspend fun updateUser(user: User)
}
