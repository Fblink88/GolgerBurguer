package com.example.goldenburgers.view


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.goldenburgers.model.SessionManager
import com.example.goldenburgers.navigation.AppScreens
import com.example.goldenburgers.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

/**
 * [ACTUALIZADO] Reemplazados los componentes obsoletos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStep5Screen(
    navController: NavController,
    viewModel: RegisterViewModel,
    sessionManager: SessionManager
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen de Registro") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }
            )
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize().padding(paddingValues), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // [CORREGIDO] Se pasa el progreso como una lambda.
                LinearProgressIndicator(progress = { 1.0f }, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp))

                Text("¡Todo listo!", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text("Verifica la información antes de finalizar.", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
                Spacer(Modifier.height(32.dp))

                LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    item { SummarySectionTitle("1. Acceso y Contacto") }
                    item { SummaryItem("Email", uiState.email) }
                    item { SummaryItem("Contraseña", "********") }
                    item { HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp)) } // <-- CORREGIDO
                    item { SummaryItem("Nombre Completo", "${uiState.fullName} ") }
                    item { SummaryItem("Teléfono", uiState.phoneNumber) }
                    item { HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp)) } // <-- CORREGIDO

                    item { SummarySectionTitle("2. Dirección de Despacho") }
                    item { SummaryItem("Calle y Número", "${uiState.street} ${uiState.number}") }
                    item { SummaryItem("Ciudad/Comuna", uiState.city) }
                    item { HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp)) } // <-- CORREGIDO

                    item { SummarySectionTitle("3. Detalles Opcionales") }
                    item { SummaryItem("Género", uiState.gender.ifBlank { "No especificado" }) }
                    item { SummaryItem("Fecha Nacimiento", uiState.birthDate.ifBlank { "No especificado" }) }
                    item { Spacer(Modifier.height(24.dp)) }
                }

                Button(
                    onClick = {
                        viewModel.onRegisterClicked(
                            onSuccess = {
                                scope.launch {
                                    sessionManager.saveUserSession(uiState.email)
                                    navController.navigate("main_flow") {
                                        popUpTo(AppScreens.WelcomeScreen.route) { inclusive = true }
                                    }
                                }
                            },
                            onError = { errorMessage ->
                                val message = if (errorMessage.contains("UNIQUE constraint failed")) {
                                    "El correo electrónico ya está registrado."
                                } else {
                                    "Error: $errorMessage"
                                }
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Confirmar Registro", style = MaterialTheme.typography.labelLarge)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SummarySectionTitle(title: String) {
    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 4.dp))
}

@Composable
private fun SummaryItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.End, modifier = Modifier.weight(2f))
    }
}
