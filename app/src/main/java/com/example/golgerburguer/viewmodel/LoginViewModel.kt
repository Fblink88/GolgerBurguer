package com.example.golgerburguer.viewmodel




import android.util.Patterns // Para la validación de email
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


/**
 * Data class que representa el estado de la interfaz de usuario para la pantalla de Login.
 * @property email El texto actual en el campo de correo electrónico.
 * @property password El texto actual en el campo de contraseña.
 * @property emailError Mensaje de error para el campo email (null si no hay error).
 * @property passwordError Mensaje de error para el campo contraseña (null si no hay error).
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null
)


/**
 * ViewModel para gestionar la lógica y el estado de la pantalla de Login.
 */
class LoginViewModel : ViewModel() {


    // _uiState: Flujo mutable y privado que contiene el estado actual.
    private val _uiState = MutableStateFlow(LoginUiState())
    // uiState: Flujo público e inmutable que la pantalla observa.
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    /**
     * Se llama cuando el texto en el campo de email cambia.
     * Actualiza el estado 'email' y realiza la validación de formato.
     * @param email El nuevo texto introducido por el usuario.
     */
    fun onEmailChange(email: String) {
        _uiState.update { currentState ->
            // Validación: Comprueba si el email no está vacío y si tiene un formato válido.
            val emailError = if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                "El formato del correo no es válido"
            } else {
                null // No hay error
            }
            currentState.copy(email = email, emailError = emailError)
        }
    }


    /**
     * Se llama cuando el texto en el campo de contraseña cambia.
     * Actualiza el estado 'password' y realiza la validación de longitud.
     * @param password El nuevo texto introducido por el usuario.
     */
    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            // Validación: Comprueba si la contraseña no está vacía y tiene al menos 6 caracteres.
            val passwordError = if (password.isNotBlank() && password.length < 6) {
                "La contraseña debe tener al menos 6 caracteres"
            } else {
                null // No hay error
            }
            currentState.copy(password = password, passwordError = passwordError)
        }
    }


    // Nota: La lógica de autenticación real (llamar a un repositorio/API) iría aquí.
    // Para este proyecto, la simulación de éxito se maneja directamente en la pantalla
    // (LoginScreen.kt) verificando que los errores sean null y llamando a SessionManager.
}





