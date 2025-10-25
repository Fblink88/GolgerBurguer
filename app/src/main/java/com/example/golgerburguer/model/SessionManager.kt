package com.example.golgerburguer.data // Asegúrate que el paquete coincida


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


// Extensión para obtener la instancia de DataStore de forma segura (singleton)
// "session_prefs" es el nombre del archivo donde se guardarán las preferencias.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")


/**
 * Clase responsable de gestionar el estado de la sesión del usuario (logueado/no logueado).
 * Utiliza DataStore Preferences para guardar esta información de forma persistente.
 * @param context El contexto de la aplicación, necesario para acceder a DataStore.
 */
class SessionManager(private val context: Context) {


    // companion object para definir las claves de las preferencias de forma centralizada.
    companion object {
        // Clave para guardar el estado de login (true si está logueado, false si no).
        val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    }


    /**
     * Guarda el estado de inicio de sesión en DataStore.
     * Es una función 'suspend' porque la escritura en DataStore es asíncrona.
     * @param isLoggedIn El estado de sesión a guardar (true o false).
     */
    suspend fun saveLoginState(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }


    /**
     * Limpia la información de la sesión guardada, efectivamente cerrando la sesión del usuario.
     * Establece el estado de login a 'false'.
     * Es una función 'suspend' porque la escritura en DataStore es asíncrona.
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = false
        }
    }


    /**
     * Expone un Flow que emite el estado actual de inicio de sesión (true o false).
     * El NavHost principal observa este Flow para decidir la pantalla de inicio.
     * Si la clave no existe en DataStore, devuelve 'false' por defecto.
     */
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            // Lee el valor booleano asociado a IS_LOGGED_IN_KEY.
            // Si no existe (ej. la primera vez que se abre la app), devuelve false.
            preferences[IS_LOGGED_IN_KEY] ?: false
        }
}





