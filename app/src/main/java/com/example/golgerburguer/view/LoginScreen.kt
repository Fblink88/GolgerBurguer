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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// Quitamos import de Color si no se usa directamente
// import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.golgerburguer.data.SessionManager
import com.example.golgerburguer.navigation.AppScreens
import com.example.golgerburguer.viewmodel.LoginViewModel
import kotlinx.coroutines.launch


/**
 * Pantalla Composable para el inicio de sesión del usuario.
 * @param navController Controlador de navegación principal.
 * @param sessionManager Gestor del estado de la sesión.
 * @param loginViewModel ViewModel específico para la lógica de login.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    sessionManager: SessionManager,
    loginViewModel: LoginViewModel // Recibe el ViewModel de Login
) {
    // Observa el estado del LoginViewModel
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope() // Para lanzar la corutina de guardar sesión


    // Determina si el botón de Ingresar debe estar habilitado
    val isLoginValid = uiState.email.isNotBlank() && uiState.password.isNotBlank() &&
            uiState.emailError == null && uiState.passwordError == null


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Iniciar Sesión") },
                navigationIcon = {
                    // Botón para volver a la pantalla anterior (WelcomeScreen)
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
                // Los colores de TopAppBar los toma del tema
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Aplica padding del Scaffold
            color = MaterialTheme.colorScheme.background // Usa color de fondo del tema
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp), // Padding horizontal
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Centra verticalmente el contenido
            ) {
                // Títulos
                Text(
                    "Inicia sesión con tu perfil",
                    style = MaterialTheme.typography.headlineSmall, // Estilo del tema
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground // Color principal
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Ingrese sus datos para continuar",
                    style = MaterialTheme.typography.bodyLarge, // Estilo del tema
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Color secundario
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(48.dp)) // Espacio antes de los campos


                // Campo de Correo Electrónico
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { loginViewModel.onEmailChange(it) },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    isError = uiState.emailError != null // Marca como error si existe
                    // Los colores (borde, texto, label) los toma del tema
                )
                // Mensaje de error animado para el correo
                AnimatedVisibility(visible = uiState.emailError != null) {
                    Text(
                        uiState.emailError ?: "",
                        color = MaterialTheme.colorScheme.error, // Color de error del tema
                        style = MaterialTheme.typography.bodySmall, // Estilo pequeño para errores
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp)
                    )
                }
                Spacer(Modifier.height(16.dp)) // Espacio entre campos


                // Campo de Contraseña
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { loginViewModel.onPasswordChange(it) },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(), // Oculta la contraseña
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    isError = uiState.passwordError != null // Marca como error si existe
                )
                // Mensaje de error animado para la contraseña
                AnimatedVisibility(visible = uiState.passwordError != null) {
                    Text(
                        uiState.passwordError ?: "",
                        color = MaterialTheme.colorScheme.error, // Color de error del tema
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp)
                    )
                }
                // TODO: Considerar añadir un botón "¿Olvidaste tu contraseña?" aquí si es necesario


                Spacer(Modifier.height(32.dp)) // Espacio antes del botón principal


                // Botón de Ingresar
                Button(
                    onClick = {
                        // Lógica al presionar Ingresar
                        // Aquí iría la llamada real al ViewModel para autenticar
                        // Por ahora, simulamos éxito si los campos son válidos
                        if (isLoginValid) {
                            scope.launch {
                                sessionManager.saveLoginState(true) // Guarda estado de logueado
                            }
                            // Navega al flujo principal y limpia el historial de login/welcome
                            navController.navigate("main_flow") {
                                popUpTo(AppScreens.WelcomeScreen.route) { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = isLoginValid // Habilita el botón solo si los datos son válidos
                    // Colores por defecto del tema
                ) {
                    Text(
                        "Ingresar",
                        style = MaterialTheme.typography.labelLarge // Estilo para texto de botones
                    )
                }
                Spacer(Modifier.height(24.dp)) // Espacio antes del enlace de registro


                // Enlace para ir a la pantalla de Registro
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "¿No tienes una cuenta?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.width(4.dp))
                    TextButton(onClick = { navController.navigate(AppScreens.RegisterStep1Screen.route) }) {
                        Text(
                            "Registrar",
                            fontWeight = FontWeight.Bold, // Resalta el texto del botón
                            style = MaterialTheme.typography.bodyMedium
                            // El color del TextButton lo toma del tema (usualmente primary)
                        )
                    }
                }
            }
        }
    }
}





