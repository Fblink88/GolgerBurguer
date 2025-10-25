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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.golgerburguer.navigation.AppScreens
import com.example.golgerburguer.viewmodel.RegisterViewModel


/**
 * Pantalla Composable para el tercer paso del registro (Dirección).
 * @param navController Controlador de navegación.
 * @param viewModel ViewModel que gestiona el estado del registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStep3Screen(navController: NavController, viewModel: RegisterViewModel) {


    // Observa el estado del RegisterViewModel de forma segura para el ciclo de vida.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    // Determina si el botón "Siguiente" debe estar habilitado.
    // Requiere que todos los campos obligatorios tengan texto y no haya errores.
    val isStep3Valid = uiState.street.isNotBlank() &&
            uiState.number.isNotBlank() &&
            uiState.commune.isNotBlank() && // Añadido
            uiState.city.isNotBlank() &&
            uiState.region.isNotBlank() && // Añadido
            uiState.streetError == null &&
            uiState.numberError == null &&
            uiState.communeError == null && // Añadido
            uiState.cityError == null &&
            uiState.regionError == null // Añadido


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Domicilio") },
                navigationIcon = {
                    // Botón para volver al paso anterior (RegisterStep2Screen).
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al Paso 2"
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
                    .padding(horizontal = 32.dp)
                    .imePadding(), // Manejo de teclado
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Indicador de progreso (Aproximadamente 3 de 5 pasos completados)
                LinearProgressIndicator(
                    progress = 0.6f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )


                Text(
                    text = "¿Dónde vives?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Necesitamos tu dirección para el despacho.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(48.dp))


                // 1. Campo de texto para la Calle/Avenida.
                OutlinedTextField(
                    value = uiState.street,
                    onValueChange = { viewModel.onStreetChange(it) },
                    label = { Text("Calle o Avenida") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    singleLine = true,
                    isError = uiState.streetError != null
                )
                AnimatedVisibility(visible = uiState.streetError != null) {
                    Text(
                        text = uiState.streetError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))


                // 2. Campos de texto para Número y Comuna en una fila.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Campo de texto para el Número de Calle.
                    OutlinedTextField(
                        value = uiState.number,
                        onValueChange = { viewModel.onNumberChange(it) },
                        label = { Text("Número") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = uiState.numberError != null
                    )
                    Spacer(modifier = Modifier.width(16.dp))


                    // Campo de texto para la Comuna.
                    OutlinedTextField(
                        value = uiState.commune,
                        onValueChange = { viewModel.onCommuneChange(it) },
                        label = { Text("Comuna") },
                        modifier = Modifier.weight(2f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        singleLine = true,
                        isError = uiState.communeError != null
                    )
                }


                // Muestra errores de Número y Comuna
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Error de Número (Ajuste para AnimatedVisibility)
                    Box(modifier = Modifier.weight(1f)) {
                        Column {
                            AnimatedVisibility(visible = uiState.numberError != null) {
                                Text(
                                    text = uiState.numberError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    // Error de Comuna (Ajuste para AnimatedVisibility)
                    Box(modifier = Modifier.weight(2f)) {
                        Column {
                            AnimatedVisibility(visible = uiState.communeError != null) {
                                Text(
                                    text = uiState.communeError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))


                // 3. Campos de texto para Ciudad y Región en una fila.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Campo de texto para la Ciudad.
                    OutlinedTextField(
                        value = uiState.city,
                        onValueChange = { viewModel.onCityChange(it) },
                        label = { Text("Ciudad") },
                        modifier = Modifier.weight(2f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        singleLine = true,
                        isError = uiState.cityError != null
                    )
                    Spacer(modifier = Modifier.width(16.dp))


                    // Campo de texto para la Región.
                    OutlinedTextField(
                        value = uiState.region,
                        onValueChange = { viewModel.onRegionChange(it) },
                        label = { Text("Región") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        singleLine = true,
                        isError = uiState.regionError != null
                    )
                }


                // Muestra errores de Ciudad y Región
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Error de Ciudad (Ajuste para AnimatedVisibility)
                    Box(modifier = Modifier.weight(2f)) {
                        Column {
                            AnimatedVisibility(visible = uiState.cityError != null) {
                                Text(
                                    text = uiState.cityError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    // Error de Región (Ajuste para AnimatedVisibility)
                    Box(modifier = Modifier.weight(1f)) {
                        Column {
                            AnimatedVisibility(visible = uiState.regionError != null) {
                                Text(
                                    text = uiState.regionError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))


                // Campo de texto opcional para Referencia Adicional (Si está en el ViewModel).
                // Nota: Asumo que tienes un campo 'additionalReference' en tu RegisterUiState.
                // Si no existe, reemplázalo por un Spacer o bórralo.
                OutlinedTextField(
                    // Placeholder: Si no tienes additionalReference, usa el campo 'city' o 'street'
                    // para evitar el error de variable no definida, o bórralo.
                    value = "", // Reemplazar con uiState.additionalReference si existe
                    onValueChange = { /* viewModel.onAdditionalReferenceChange(it) */ }, // Reemplazar con función si existe
                    label = { Text("Referencia Adicional (Opcional)") },
                    placeholder = { Text("Ej: Depto 501, Entrada B, Casa azul") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(32.dp))


                // Botón para ir al siguiente paso del registro (Paso 4).
                Button(
                    onClick = {
                        // Navega al Paso 4 si los datos son válidos.
                        if (isStep3Valid) {
                            navController.navigate(AppScreens.RegisterStep4Screen.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = isStep3Valid // Habilita el botón solo si los datos son válidos.
                ) {
                    Text(
                        "Siguiente",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}





