package com.example.golgerburguer.view




import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// Quitamos import de Color si no se usa directamente
// import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
// Quitamos import de sp si usamos estilos del tema
// import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.golgerburguer.navigation.AppScreens
import com.example.golgerburguer.viewmodel.RegisterViewModel


/**
 * Pantalla Composable para el primer paso del registro (Credenciales).
 * @param navController Controlador de navegación.
 * @param viewModel ViewModel que gestiona el estado del registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStep1Screen(navController: NavController, viewModel: RegisterViewModel) {


    // Observa el estado del RegisterViewModel de forma segura para el ciclo de vida.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    // Determina si el botón "Siguiente" debe estar habilitado.
    // Requiere que ambos campos tengan texto y no haya errores de validación activos.
    val isStep1Valid = uiState.email.isNotBlank() &&
            uiState.password.isNotBlank() &&
            uiState.emailError == null &&
            uiState.passwordError == null


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    // Botón para volver a la pantalla anterior (WelcomeScreen).
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás" // Descripción para accesibilidad
                        )
                    }
                }
                // Los colores de TopAppBar los toma del tema.
            )
        }
    ) { paddingValues ->
        // Surface para aplicar el color de fondo del tema.
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Aplica padding del Scaffold.
            color = MaterialTheme.colorScheme.background // Color de fondo del tema.
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp), // Padding horizontal.
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Centra el contenido verticalmente.
            ) {
                // Títulos informativos.
                Text(
                    text = "Regístrate para continuar",
                    style = MaterialTheme.typography.headlineSmall, // Estilo del tema.
                    color = MaterialTheme.colorScheme.onBackground, // Color principal de texto.
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ingresa tu correo y crea una contraseña",
                    style = MaterialTheme.typography.bodyLarge, // Estilo del tema.
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Color secundario.
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(48.dp)) // Espacio antes de los campos.


                // Campo de texto para el Correo Electrónico.
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.onEmailChange(it) }, // Llama al ViewModel al cambiar texto.
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho.
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // Teclado de email.
                    singleLine = true, // Campo de una sola línea.
                    isError = uiState.emailError != null // Marca el campo si hay error.
                    // Los colores los toma del tema.
                )
                // Muestra el mensaje de error de email con animación.
                AnimatedVisibility(visible = uiState.emailError != null) {
                    Text(
                        text = uiState.emailError ?: "", // Muestra el mensaje de error.
                        color = MaterialTheme.colorScheme.error, // Color de error del tema.
                        style = MaterialTheme.typography.bodySmall, // Estilo pequeño para errores.
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp) // Alinea con el texto del campo.
                    )
                }
                Spacer(modifier = Modifier.height(16.dp)) // Espacio entre campos.


                // Campo de texto para la Contraseña.
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChange(it) }, // Llama al ViewModel.
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(), // Oculta la contraseña.
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // Teclado de contraseña.
                    singleLine = true,
                    isError = uiState.passwordError != null // Marca el campo si hay error.
                )
                // Muestra el mensaje de error de contraseña con animación.
                AnimatedVisibility(visible = uiState.passwordError != null) {
                    Text(
                        text = uiState.passwordError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp)) // Espacio antes del botón principal.


                // Botón para ir al siguiente paso del registro.
                Button(
                    onClick = {
                        // Navega al Paso 2 si los datos son válidos.
                        if (isStep1Valid) {
                            navController.navigate(AppScreens.RegisterStep2Screen.route)
                        }
                        // Nota: Se podría añadir una validación extra aquí si se quisiera,
                        //       pero la habilitación del botón ya la controla.
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp), // Altura fija.
                    enabled = isStep1Valid // Habilita el botón solo si los datos son válidos.
                    // Colores por defecto del tema.
                ) {
                    Text(
                        "Siguiente",
                        style = MaterialTheme.typography.labelLarge // Estilo para texto de botones.
                    )
                }


                Spacer(modifier = Modifier.height(24.dp)) // Espacio antes del enlace de login.


                // Enlace para ir a la pantalla de Login.
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "¿Ya tienes una cuenta?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = { navController.navigate(AppScreens.LoginScreen.route) }) {
                        Text(
                            "Iniciar Sesión",
                            fontWeight = FontWeight.Bold, // Resalta el texto del botón.
                            style = MaterialTheme.typography.bodyMedium
                            // El color del TextButton lo toma del tema.
                        )
                    }
                }
            }
        }
    }
}





