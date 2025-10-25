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
    // Requiere que la fecha de nacimiento esté seleccionada y no haya errores.
    val isStep4Valid = uiState.birthDate.isNotBlank() && uiState.birthDateError == null


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles Adicionales") },
                navigationIcon = {
                    // Botón para volver al paso anterior (RegisterStep3Screen).
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
                // Indicador de progreso (Aproximadamente 4 de 5 pasos completados)
                LinearProgressIndicator(
                    progress = { 0.8f }, // Usando la sintaxis de lambda para evitar advertencia de deprecación
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
                            onClick = { viewModel.onGenderChange(label) }, // Actualiza el ViewModel
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


                // Campo de texto que se comporta como botón para abrir el selector de fecha.
                OutlinedTextField(
                    value = uiState.birthDate,
                    onValueChange = { /* Solo se cambia con el DatePicker */ },
                    label = { Text("Selecciona tu fecha") },
                    readOnly = true,
                    isError = uiState.birthDateError != null,
                    trailingIcon = {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = "Abrir Selector de Fecha",
                            modifier = Modifier.clickable { showDatePicker = true } // Abre el diálogo al hacer clic en el icono.
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true } // Abre el diálogo al hacer clic en el campo.
                )
                // Muestra el mensaje de error de fecha.
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


                // Botón para ir al siguiente paso del registro (Paso 5/Resumen).
                Button(
                    onClick = {
                        // Navega al Paso 5 si los datos son válidos.
                        if (isStep4Valid) {
                            navController.navigate(AppScreens.RegisterStep5Screen.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = isStep4Valid // Habilita el botón solo si los datos son válidos.
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


    // --- Diálogo de Selección de Fecha (DatePickerDialog) ---
    if (showDatePicker) {
        // Calculamos la fecha máxima de selección (hace 18 años).
        val maxDate = LocalDate.now().minus(18, ChronoUnit.YEARS)
        val maxDateMillis = maxDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()


        val datePickerState = rememberDatePickerState(
            // La fecha máxima seleccionable es de hace 18 años (requisito de edad mínima).
            initialSelectedDateMillis = maxDateMillis,
            // Rango de fechas: Desde el inicio de la era (hace 100 años) hasta la fecha máxima (hace 18 años).
            yearRange = (LocalDate.now().minus(100, ChronoUnit.YEARS).year..maxDate.year),
            initialDisplayMode = DisplayMode.Picker
            // [ELIMINADO] dateValidator para evitar el error de parámetro no encontrado.
        )

        // Validamos la selección manualmente para habilitar el botón Confirmar.
        // La fecha es válida si no es nula Y no es posterior a la fecha máxima permitida (hace 18 años).
        val isDateSelectionValid = datePickerState.selectedDateMillis != null &&
                datePickerState.selectedDateMillis!! <= maxDateMillis




        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            // Convierte los milisegundos a LocalDate.
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()


                            // Formatea la fecha al formato deseado (ej: 01/01/2000)
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
                            val formattedDate = selectedDate.format(formatter)


                            // Llama al ViewModel para actualizar la fecha y validar la edad.
                            viewModel.onBirthDateChange(formattedDate)
                        }
                        showDatePicker = false // Cierra el diálogo al confirmar.
                    },
                    // [ACTUALIZADO] El botón se habilita solo si la selección cumple con la validación de edad.
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





