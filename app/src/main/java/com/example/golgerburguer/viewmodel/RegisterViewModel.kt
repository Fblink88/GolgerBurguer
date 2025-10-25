package com.example.golgerburguer.viewmodel // Asegúrate que el paquete coincida




import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update




/**
 * Data class que representa el estado completo de la UI para el flujo de registro.
 *
 * CAMBIOS REALIZADOS: Se reemplazaron firstName/lastName por fullName, se agregó phoneNumber,
 * y se añadieron los campos 'gender' (género) y 'birthdate' (fecha de nacimiento).
 */
data class RegisterUiState(
    // Paso 1: Credenciales
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,


    // Paso 2: Información Personal
    val fullName: String = "", // Campo esperado por la pantalla Step2
    val phoneNumber: String = "", // Campo esperado por la pantalla Step2
    val gender: String = "", // <- NUEVO: Género del usuario (ej: "Masculino", "Femenino", "Otro")
    val birthDate: String = "", // <- NUEVO: Fecha de nacimiento (ej: "1990-12-31")


    val fullNameError: String? = null, // Error esperado por la pantalla Step2
    val phoneNumberError: String? = null, // Error esperado por la pantalla Step2
    val genderError: String? = null, // <- NUEVO: Error para el género
    val birthDateError: String? = null, // <- NUEVO: Error para la fecha de nacimiento


    // Paso 3: Foto de Perfil
    val profileImageUri: String? = null,


    // Paso 4: Dirección
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
 * ViewModel encargado de gestionar la lógica de negocio y el estado
 * de la interfaz de usuario durante el proceso de registro.
 */
class RegisterViewModel : ViewModel() {




    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()




    // --- LÓGICA DE VALIDACIÓN ---




    private fun validateFullName(name: String): String? {
        return if (name.isBlank()) {
            "El nombre completo no puede estar vacío"
        } else if (name.length < 5) {
            "El nombre es demasiado corto"
        } else {
            null
        }
    }




    private fun validatePhoneNumber(phone: String): String? {
        // Validación básica para Chile: 9 dígitos (ej: 912345678)
        return if (phone.isBlank()) {
            "El número de teléfono es obligatorio"
        } else if (phone.length != 9 || !phone.all { it.isDigit() }) {
            "Debe ser un número de 9 dígitos (ej. 912345678)"
        } else {
            null
        }
    }


    /**
     * Valida que el campo de género no esté vacío.
     */
    private fun validateGender(gender: String): String? {
        return if (gender.isBlank()) {
            "El género es obligatorio"
        } else {
            null
        }
    }


    /**
     * Valida que la fecha de nacimiento no esté vacía y tenga un formato básico YYYY-MM-DD.
     */
    private fun validateBirthDate(date: String): String? {
        // Regex simple para formato AAAA-MM-DD (no valida la validez de la fecha en sí, solo el formato)
        val dateRegex = "^\\d{4}-\\d{2}-\\d{2}$".toRegex()


        return if (date.isBlank()) {
            "La fecha de nacimiento es obligatoria"
        } else if (!date.matches(dateRegex)) {
            "El formato debe ser AAAA-MM-DD"
        } else {
            // Nota: Se podría añadir lógica para verificar si es una fecha pasada,
            // o si el usuario es mayor de edad, etc.
            null
        }
    }




    // Validaciones de Dirección (Puedes expandirlas si lo necesitas)
    private fun validateNumber(number: String): String? {
        return if (number.isBlank()) {
            "El número de dirección es obligatorio"
        } else if (!number.all { it.isDigit() }) {
            "Solo se permiten números"
        } else {
            null
        }
    }




    // --- Funciones llamadas por la UI para actualizar el estado ---




    // Paso 1: Credenciales
    fun onEmailChange(email: String) {
        _uiState.update { currentState ->
            val error = if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                "El formato del correo no es válido"
            } else if (email.isBlank()) {
                "El correo no puede estar vacío"
            } else {
                null
            }
            currentState.copy(email = email, emailError = error)
        }
    }




    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            val error = if (password.isNotBlank() && password.length < 6) {
                "La contraseña debe tener al menos 6 caracteres"
            } else if (password.isBlank()) {
                "La contraseña no puede estar vacía"
            } else {
                null
            }
            currentState.copy(password = password, passwordError = error)
        }
    }




    // Paso 2: Información Personal
    fun onFullNameChange(fullName: String) {
        _uiState.update { currentState ->
            val error = validateFullName(fullName)
            currentState.copy(fullName = fullName, fullNameError = error)
        }
    }




    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.update { currentState ->
            val error = validatePhoneNumber(phoneNumber)
            currentState.copy(phoneNumber = phoneNumber, phoneNumberError = error)
        }
    }


    /**
     * Actualiza el género y valida el campo.
     */
    fun onGenderChange(gender: String) {
        _uiState.update { currentState ->
            val error = validateGender(gender)
            currentState.copy(gender = gender, genderError = error)
        }
    }


    /**
     * Actualiza la fecha de nacimiento y valida el campo.
     */
    fun onBirthDateChange(birthdate: String) {
        _uiState.update { currentState ->
            val error = validateBirthDate(birthdate)
            currentState.copy(birthDate = birthdate, birthDateError = error)
        }
    }




    // Paso 3: Foto
    fun onProfileImageChange(uri: String?) {
        _uiState.update { currentState ->
            currentState.copy(profileImageUri = uri)
        }
    }




    // Paso 4: Dirección


    /**
     * Actualiza la calle y valida el campo.
     */
    fun onStreetChange(street: String) {
        _uiState.update { currentState ->
            val error = if (street.isBlank()) "La calle no puede estar vacía" else null
            currentState.copy(street = street, streetError = error)
        }
    }


    /**
     * Actualiza el número de dirección y valida el campo.
     */
    fun onNumberChange(number: String) {
        _uiState.update { currentState ->
            val error = validateNumber(number)
            currentState.copy(number = number, numberError = error)
        }
    }




    fun onCommuneChange(commune: String) {
        _uiState.update { currentState ->
            val error = if (commune.isBlank()) "La comuna no puede estar vacía" else null
            currentState.copy(commune = commune, communeError = error)
        }
    }




    fun onCityChange(city: String) {
        _uiState.update { currentState ->
            val error = if (city.isBlank()) "La ciudad no puede estar vacía" else null
            currentState.copy(city = city, cityError = error)
        }
    }




    fun onRegionChange(region: String) {
        _uiState.update { currentState ->
            val error = if (region.isBlank()) "La región no puede estar vacía" else null
            currentState.copy(region = region, regionError = error)
        }
    }




    /**
     * Función (a implementar) que se llamaría al finalizar el registro.
     */
    fun onRegisterClicked() {
        val currentState = _uiState.value
        // Esta validación debe ser revisada después de implementar todos los pasos
        if (
            currentState.emailError == null && currentState.passwordError == null &&
            currentState.fullNameError == null && currentState.phoneNumberError == null &&
            currentState.genderError == null && currentState.birthDateError == null && // <- NUEVOS CHEQUEOS
            currentState.streetError == null && currentState.numberError == null &&
            currentState.communeError == null && currentState.cityError == null &&
            currentState.regionError == null &&
            // Asegurarse que los campos obligatorios no estén vacíos
            currentState.email.isNotBlank() && currentState.password.isNotBlank() &&
            currentState.fullName.isNotBlank() && currentState.phoneNumber.isNotBlank() &&
            currentState.gender.isNotBlank() && currentState.birthDate.isNotBlank() && // <- NUEVOS CHEQUEOS
            currentState.street.isNotBlank() && currentState.number.isNotBlank() &&
            currentState.commune.isNotBlank() && currentState.city.isNotBlank() &&
            currentState.region.isNotBlank()
        ) {
            // TODO: Lógica para guardar el usuario registrado
            println("Registro Válido: $currentState")
        } else {
            println("Error en el registro: Faltan datos o hay errores.")
        }
    }
}





