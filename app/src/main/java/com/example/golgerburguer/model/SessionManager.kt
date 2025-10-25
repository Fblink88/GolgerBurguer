package com.example.golgerburguer.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Crea una extensión de Context para acceder a DataStore de forma centralizada.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

/**
 * Gestiona el estado de la sesión del usuario (logueado/no logueado) usando Jetpack DataStore.
 *
 * @param context El contexto de la aplicación, necesario para inicializar DataStore.
 */
class SessionManager(private val context: Context) {

    // Define la clave para almacenar el estado de inicio de sesión.
    private val isLoggedInKey = booleanPreferencesKey("is_logged_in")

    /**
     * Un Flow que emite `true` si el usuario ha iniciado sesión y `false` en caso contrario.
     * Observa cambios en tiempo real en el estado de la sesión.
     */
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            // Lee el valor booleano. Si no existe, devuelve 'false' por defecto.
            preferences[isLoggedInKey] ?: false
        }

    /**
     * Guarda el estado de la sesión como "logueado".
     * Esta es una función suspendida porque DataStore opera de forma asíncrona.
     */
    suspend fun setLoggedIn() {
        context.dataStore.edit { preferences ->
            preferences[isLoggedInKey] = true
        }
    }

    /**
     * Borra el estado de la sesión, marcando al usuario como "no logueado".
     * También es una función suspendida.
     */
    suspend fun setLoggedOut() {
        context.dataStore.edit { preferences ->
            preferences[isLoggedInKey] = false
        }
    }
}
