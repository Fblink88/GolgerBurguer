package com.example.golgerburguer.view




import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.golgerburguer.navigation.AppScreens
import com.example.golgerburguer.viewmodel.RegisterViewModel


/**
 * Pantalla Composable para el segundo paso del registro (Información Personal).
 * @param navController Controlador de navegación.
 * @param viewModel ViewModel que gestiona el estado del registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStep2Screen(navController: NavController, viewModel: RegisterViewModel) {


    // Observa el estado del RegisterViewModel de forma segura para el ciclo de vida.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    // Determina si el botón "Siguiente" debe estar habilitado.
    // Requiere que ambos campos tengan texto y no haya errores de validación activos.
    val isStep2Valid = uiState.fullName.isNotBlank() &&
            uiState.phoneNumber.isNotBlank() &&
            uiState.fullNameError == null &&
            uiState.phoneNumberError == null


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Información Personal") },
                navigationIcon = {
                    // Botón para volver al paso anterior (RegisterStep1Screen).
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al Paso 1"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Indicador de progreso (Opcional, pero útil en flujos multipaso)
                LinearProgressIndicator(
                    progress = 0.4f, // Aproximadamente 2 de 5 pasos completados
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.primary // Color principal del tema
                )


                Text(
                    text = "Cuéntanos más sobre ti",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Necesitamos tu nombre y teléfono de contacto.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(48.dp))


                // Campo de texto para el Nombre Completo.
                OutlinedTextField(
                    value = uiState.fullName,
                    onValueChange = { viewModel.onFullNameChange(it) }, // Llama al ViewModel.
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), // Teclado normal.
                    singleLine = true,
                    isError = uiState.fullNameError != null
                )
                // Muestra el mensaje de error de nombre.
                AnimatedVisibility(visible = uiState.fullNameError != null) {
                    Text(
                        text = uiState.fullNameError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))


                // Campo de texto para el Número de Teléfono.
                OutlinedTextField(
                    value = uiState.phoneNumber,
                    onValueChange = { viewModel.onPhoneNumberChange(it) }, // Llama al ViewModel.
                    label = { Text("Número de Teléfono") },
                    placeholder = { Text("Ej: 56912345678") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), // Teclado numérico de teléfono.
                    singleLine = true,
                    isError = uiState.phoneNumberError != null
                )
                // Muestra el mensaje de error de teléfono.
                AnimatedVisibility(visible = uiState.phoneNumberError != null) {
                    Text(
                        text = uiState.phoneNumberError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp)
                    )
                }


                Spacer(modifier = Modifier.height(32.dp))


                // Botón para ir al siguiente paso del registro (Paso 3).
                Button(
                    onClick = {
                        // Navega al Paso 3 si los datos son válidos.
                        if (isStep2Valid) {
                            navController.navigate(AppScreens.RegisterStep3Screen.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = isStep2Valid // Habilita el botón solo si los datos son válidos.
                ) {
                    Text(
                        "Siguiente",
                        style = MaterialTheme.typography.labelLarge
                    )
                }


                Spacer(modifier = Modifier.height(24.dp))
                // Espacio inferior para evitar que el contenido toque el borde.
            }
        }
    }
}



