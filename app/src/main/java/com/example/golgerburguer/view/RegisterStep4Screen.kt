package com.example.golgerburguer.view


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.golgerburguer.navigation.AppScreens
import com.example.golgerburguer.viewmodel.RegisterViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


/**
 * Pantalla Composable para el cuarto paso del registro (Género y Fecha de Nacimiento).
 * @param navController Controlador de navegación.
 * @param viewModel ViewModel que gestiona el estado del registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStep4Screen(navController: NavController, viewModel: RegisterViewModel) {


    // Observa el estado del RegisterViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    // Estado local para controlar la visibilidad del diálogo de selección de fecha.
    var showDatePicker by remember { mutableStateOf(false) }


    // Determina si el botón "Siguiente" debe estar habilitado.
    val isStep4Valid = uiState.birthDate.isNotBlank() && uiState.birthDateError == null


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles Adicionales") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al Paso 3"
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
                LinearProgressIndicator(
                    progress = { 0.8f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )


                Text(
                    text = "Casi terminamos",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Solo necesitamos saber tu género y fecha de nacimiento.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(48.dp))


                // --- Selector de Género (Segmented Button) ---
                Text(
                    text = "Género",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))


                val genderOptions = listOf("Hombre", "Mujer", "Otro")
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    genderOptions.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.baseShape,
                            onClick = { viewModel.onGenderChange(label) },
                            selected = uiState.gender == label
                        ) {
                            Text(label)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))


                // --- Selector de Fecha de Nacimiento ---
                Text(
                    text = "Fecha de Nacimiento",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))


                OutlinedTextField(
                    value = uiState.birthDate,
                    onValueChange = {},
                    label = { Text("Selecciona tu fecha") },
                    readOnly = true,
                    isError = uiState.birthDateError != null,
                    trailingIcon = {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = "Abrir Selector de Fecha",
                            modifier = Modifier.clickable { showDatePicker = true }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                )
                AnimatedVisibility(visible = uiState.birthDateError != null) {
                    Text(
                        text = uiState.birthDateError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp)
                    )
                }


                Spacer(modifier = Modifier.height(32.dp))


                Button(
                    onClick = {
                        if (isStep4Valid) {
                            navController.navigate(AppScreens.RegisterStep5Screen.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = isStep4Valid
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


    if (showDatePicker) {
        val maxDate = LocalDate.now().minus(18, ChronoUnit.YEARS)
        val maxDateMillis = maxDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()


        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = maxDateMillis,
            yearRange = (LocalDate.now().minus(100, ChronoUnit.YEARS).year..maxDate.year),
            initialDisplayMode = DisplayMode.Picker
        )


        val isDateSelectionValid = datePickerState.selectedDateMillis != null &&
                datePickerState.selectedDateMillis!! <= maxDateMillis


        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()


                            // [CORRECCIÓN FINAL] Formatea la fecha al formato AAAA-MM-DD.
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
                            val formattedDate = selectedDate.format(formatter)


                            // Llama al ViewModel con la fecha en el formato correcto.
                            viewModel.onBirthDateChange(formattedDate)
                        }
                        showDatePicker = false
                    },
                    enabled = isDateSelectionValid
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
