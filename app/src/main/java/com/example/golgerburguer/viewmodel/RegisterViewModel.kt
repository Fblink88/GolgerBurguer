package com.example.golgerburguer.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golgerburguer.model.ProductRepository
import com.example.golgerburguer.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Data class que representa el estado completo de la UI para el flujo de registro.
 */
data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val fullName: String = "",
    val phoneNumber: String = "",
    val gender: String = "",
    val birthDate: String = "",
    val fullNameError: String? = null,
    val phoneNumberError: String? = null,
    val genderError: String? = null,
    val birthDateError: String? = null,
    val profileImageUri: String? = null,
    val number: String = "",
    val street: String = "",
    val commune: String = "",
    val city: String = "",
    val region: String = "",
    val streetError: String? = null,
    val numberError: String? = null,
    val communeError: String? = null,
    val cityError: String? = null,
    val regionError: String? = null
)

/**
 * ViewModel para el flujo de registro.
 * [ACTUALIZADO] Ahora recibe el repositorio para poder guardar el usuario.
 */
class RegisterViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // --- LÓGICA DE VALIDACIÓN (sin cambios) ---

    private fun validateFullName(name: String): String? {
        return if (name.isBlank()) "El nombre completo no puede estar vacío" else if (name.length < 5) "El nombre es demasiado corto" else null
    }

    private fun validatePhoneNumber(phone: String): String? {
        return if (phone.isBlank()) "El número de teléfono es obligatorio" else if (phone.length != 9 || !phone.all { it.isDigit() }) "Debe ser un número de 9 dígitos (ej. 912345678)" else null
    }

    private fun validateGender(gender: String): String? {
        return if (gender.isBlank()) "El género es obligatorio" else null
    }

    private fun validateBirthDate(date: String): String? {
        val dateRegex = "^\\d{4}-\\d{2}-\\d{2}$".toRegex()
        return if (date.isBlank()) "La fecha de nacimiento es obligatoria" else if (!date.matches(dateRegex)) "El formato debe ser AAAA-MM-DD" else null
    }

    private fun validateNumber(number: String): String? {
        return if (number.isBlank()) "El número de dirección es obligatorio" else if (!number.all { it.isDigit() }) "Solo se permiten números" else null
    }

    // --- Actualizadores de estado (sin cambios) ---

    fun onEmailChange(email: String) {
        val error = if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) "El formato del correo no es válido" else if (email.isBlank()) "El correo no puede estar vacío" else null
        _uiState.update { it.copy(email = email, emailError = error) }
    }

    fun onPasswordChange(password: String) {
        val error = if (password.isNotBlank() && password.length < 6) "La contraseña debe tener al menos 6 caracteres" else if (password.isBlank()) "La contraseña no puede estar vacía" else null
        _uiState.update { it.copy(password = password, passwordError = error) }
    }

    fun onFullNameChange(fullName: String) = _uiState.update { it.copy(fullName = fullName, fullNameError = validateFullName(fullName)) }
    fun onPhoneNumberChange(phoneNumber: String) = _uiState.update { it.copy(phoneNumber = phoneNumber, phoneNumberError = validatePhoneNumber(phoneNumber)) }
    fun onGenderChange(gender: String) = _uiState.update { it.copy(gender = gender, genderError = validateGender(gender)) }
    fun onBirthDateChange(birthdate: String) = _uiState.update { it.copy(birthDate = birthdate, birthDateError = validateBirthDate(birthdate)) }
    fun onProfileImageChange(uri: String?) = _uiState.update { it.copy(profileImageUri = uri) }
    fun onStreetChange(street: String) = _uiState.update { it.copy(street = street, streetError = if (street.isBlank()) "La calle no puede estar vacía" else null) }
    fun onNumberChange(number: String) = _uiState.update { it.copy(number = number, numberError = validateNumber(number)) }
    fun onCommuneChange(commune: String) = _uiState.update { it.copy(commune = commune, communeError = if (commune.isBlank()) "La comuna no puede estar vacía" else null) }
    fun onCityChange(city: String) = _uiState.update { it.copy(city = city, cityError = if (city.isBlank()) "La ciudad no puede estar vacía" else null) }
    fun onRegionChange(region: String) = _uiState.update { it.copy(region = region, regionError = if (region.isBlank()) "La región no puede estar vacía" else null) }


    /**
     * [ACTUALIZADO] Función que se llama al finalizar el registro.
     * Crea un objeto User y lo guarda en la base de datos.
     */
    fun onRegisterClicked(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (isFormValid()) {
            viewModelScope.launch {
                try {
                    val state = _uiState.value
                    val newUser = User(
                        email = state.email,
                        password = state.password, // En una app real, esto debería ser un hash
                        fullName = state.fullName,
                        phoneNumber = state.phoneNumber,
                        gender = state.gender,
                        birthDate = state.birthDate,
                        street = state.street,
                        number = state.number,
                        city = state.city,
                        region = state.region,
                        commune = state.commune
                    )
                    repository.registerUser(newUser)
                    onSuccess() // Llama al callback de éxito
                } catch (e: Exception) {
                    // Esto puede pasar si el email ya está registrado (violación de índice único)
                    onError(e.message ?: "Ocurrió un error desconocido")
                }
            }
        } else {
            onError("El formulario contiene errores o datos incompletos.")
        }
    }

    private fun isFormValid(): Boolean {
        val state = _uiState.value
        return state.emailError == null && state.passwordError == null &&
                state.fullNameError == null && state.phoneNumberError == null &&
                state.genderError == null && state.birthDateError == null &&
                state.streetError == null && state.numberError == null &&
                state.communeError == null && state.cityError == null &&
                state.regionError == null &&
                state.email.isNotBlank() && state.password.isNotBlank() &&
                state.fullName.isNotBlank() && state.phoneNumber.isNotBlank() &&
                state.gender.isNotBlank() && state.birthDate.isNotBlank() &&
                state.street.isNotBlank() && state.number.isNotBlank() &&
                state.commune.isNotBlank() && state.city.isNotBlank() &&
                state.region.isNotBlank()
    }
}
