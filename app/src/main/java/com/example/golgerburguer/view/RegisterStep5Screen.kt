package com.example.golgerburguer.view


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.golgerburguer.navigation.AppScreens
import com.example.golgerburguer.viewmodel.RegisterViewModel


/**
 * Pantalla Composable para el quinto y último paso del registro (Resumen y Confirmación).
 * @param navController Controlador de navegación.
 * @param viewModel ViewModel que gestiona el estado del registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStep5Screen(navController: NavController, viewModel: RegisterViewModel) {


    // Observa el estado del RegisterViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen de Registro") },
                navigationIcon = {
                    // Botón para volver al paso anterior (RegisterStep4Screen) para posibles ediciones.
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al Paso 4"
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
                // Indicador de progreso (100% completado)
                LinearProgressIndicator(
                    progress = 1.0f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )


                Text(
                    text = "¡Todo listo!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Por favor, verifica la información antes de finalizar.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))


                // Área de resumen desplazable
                LazyColumn(
                    modifier = Modifier
                        .weight(1f) // Ocupa todo el espacio disponible.
                        .fillMaxWidth()
                ) {
                    // --- Sección 1: Datos de Acceso y Personales ---
                    item {
                        SummarySectionTitle(title = "1. Acceso y Contacto")
                        Spacer(modifier = Modifier.height(8.dp))
                        SummaryItem(label = "Email", value = uiState.email)
                        SummaryItem(label = "Contraseña", value = "********") // Nunca mostrar la contraseña real
                        Divider(Modifier.padding(vertical = 12.dp))
                        SummaryItem(label = "Nombre Completo", value = "${uiState.fullName} ")
                        SummaryItem(label = "Teléfono", value = uiState.phoneNumber)
                        Divider(Modifier.padding(vertical = 24.dp))
                    }


                    // --- Sección 2: Domicilio ---
                    item {
                        SummarySectionTitle(title = "2. Dirección de Despacho")
                        Spacer(modifier = Modifier.height(8.dp))
                        SummaryItem(label = "Calle y Número", value = "${uiState.street} ${uiState.number}")
                        SummaryItem(label = "Ciudad/Comuna", value = uiState.city)

                        Divider(Modifier.padding(vertical = 24.dp))
                    }


                    // --- Sección 3: Detalles Adicionales ---
                    item {
                        SummarySectionTitle(title = "3. Detalles Opcionales")
                        Spacer(modifier = Modifier.height(8.dp))
                        SummaryItem(label = "Género", value = uiState.gender.ifBlank { "No especificado" })
                        SummaryItem(label = "Fecha Nacimiento", value = uiState.birthDate.ifBlank { "No especificado" })
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }


                // Botón de Confirmación Final
                Button(
                    onClick = {
                        // Llama a la acción final del ViewModel (simulada)
                        viewModel.onRegisterClicked()
                        // Navega a la pantalla de inicio o éxito.
                        // En un caso real, esto solo ocurriría si el registro fue exitoso.
                        navController.navigate(AppScreens.HomeScreen.route) {
                            // Limpia la pila de navegación para que el usuario no pueda volver al registro
                            popUpTo(AppScreens.RegisterStep1Screen.route) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                ) {
                    Text(
                        "Confirmar Registro",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


/**
 * Componente helper para el título de una sección del resumen.
 */
@Composable
private fun SummarySectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 4.dp)
    )
}


/**
 * Componente helper para mostrar una línea de dato (Clave: Valor).
 */
@Composable
private fun SummaryItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(2f)
        )
    }
}



