package com.example.golgerburguer.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

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
