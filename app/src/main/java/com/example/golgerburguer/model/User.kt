package com.example.golgerburguer.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representa la tabla de usuarios en la base de datos.
 * Se añade un índice único en la columna 'email' para asegurar que no haya correos duplicados
 * y para optimizar las búsquedas por email.
 */
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    // Es una muy mala práctica guardar contraseñas en texto plano.
    // En una app real, aquí se guardaría un hash de la contraseña (ej: con SHA-256).
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
    val profileImageUri: String? = null // <-- NUEVO

)
