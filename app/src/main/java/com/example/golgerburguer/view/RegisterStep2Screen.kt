package com.example.golgerburguer.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
 * [ACTUALIZADO] Añadido el campo de Región que faltaba.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStep2Screen(navController: NavController, viewModel: RegisterViewModel) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // La validación ahora incluye el campo de región.
    val isStep2Valid = uiState.fullName.isNotBlank() && uiState.phoneNumber.isNotBlank() &&
            uiState.street.isNotBlank() && uiState.number.isNotBlank() &&
            uiState.city.isNotBlank() && uiState.commune.isNotBlank() && uiState.region.isNotBlank() &&
            uiState.fullNameError == null && uiState.phoneNumberError == null &&
            uiState.streetError == null && uiState.numberError == null &&
            uiState.cityError == null && uiState.communeError == null && uiState.regionError == null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos Personales y Dirección") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .verticalScroll(rememberScrollState()), // Permite hacer scroll
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(progress = { 0.4f }, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp))

                Text("Ahora, un poco sobre ti", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text("Necesitamos tus datos personales y de despacho.", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(32.dp))

                // --- Campos de Datos Personales ---
                OutlinedTextField(value = uiState.fullName, onValueChange = { viewModel.onFullNameChange(it) }, label = { Text("Nombre Completo") }, isError = uiState.fullNameError != null, singleLine = true, modifier = Modifier.fillMaxWidth())
                AnimatedVisibility(visible = uiState.fullNameError != null) {
                    Text(uiState.fullNameError ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 4.dp))
                }
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = uiState.phoneNumber, onValueChange = { viewModel.onPhoneNumberChange(it) }, label = { Text("Teléfono (9 dígitos)") }, isError = uiState.phoneNumberError != null, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                AnimatedVisibility(visible = uiState.phoneNumberError != null) {
                    Text(uiState.phoneNumberError ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 4.dp))
                }
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

                // --- Campos de Dirección ---
                Text("Dirección de Despacho", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(value = uiState.street, onValueChange = { viewModel.onStreetChange(it) }, label = { Text("Calle") }, isError = uiState.streetError != null, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = uiState.number, onValueChange = { viewModel.onNumberChange(it) }, label = { Text("Número") }, isError = uiState.numberError != null, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = uiState.commune, onValueChange = { viewModel.onCommuneChange(it) }, label = { Text("Comuna") }, isError = uiState.communeError != null, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = uiState.city, onValueChange = { viewModel.onCityChange(it) }, label = { Text("Ciudad") }, isError = uiState.cityError != null, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = uiState.region, onValueChange = { viewModel.onRegionChange(it) }, label = { Text("Región") }, isError = uiState.regionError != null, singleLine = true, modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = { if (isStep2Valid) { navController.navigate(AppScreens.RegisterStep3Screen.route) } },
                    enabled = isStep2Valid,
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Siguiente")
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}