package com.example.golgerburguer.view




import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// Quitamos import no usado
// import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.golgerburguer.data.SessionManager
import com.example.golgerburguer.navigation.AppScreens
import kotlinx.coroutines.launch


/**
 * Pantalla Composable que muestra el perfil del usuario y permite cerrar sesión.
 * @param navController El NavController principal (de AppNavigation) para poder volver a Welcome.
 * @param sessionManager El gestor de sesión para poder llamar a clearSession().
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, sessionManager: SessionManager) {


    // Scope para lanzar la corutina de clearSession
    val scope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    // Botón para volver a la pantalla anterior dentro del BottomNav (ej. Home)
                    // NOTA: Si quisiéramos volver a Home directamente, podríamos usar
                    // navController.navigate(BottomNavItem.Home.route) { popUpTo(...) }
                    // pero popBackStack es más general si llegamos desde otra pestaña.
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
                // Los colores de TopAppBar los toma del tema
            )
        }
    ) { paddingValues ->
        // Usamos Surface para aplicar el color de fondo del tema
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Aplica padding del Scaffold
            color = MaterialTheme.colorScheme.background // Aplicamos color de fondo del tema
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Padding interno del contenido
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Centra el contenido
            ) {
                // Título de la sección
                Text(
                    "Configuración de la cuenta",
                    // Usamos estilo y color del tema
                    style = MaterialTheme.typography.headlineSmall, // Estilo para títulos pequeños
                    color = MaterialTheme.colorScheme.onBackground // Color de texto principal
                )


                Spacer(modifier = Modifier.height(48.dp)) // Espacio antes del botón


                // Botón de Cerrar Sesión
                Button(
                    onClick = {
                        // Inicia la corutina para limpiar la sesión en DataStore
                        scope.launch {
                            sessionManager.clearSession()
                        }
                        // Navega a la pantalla de bienvenida
                        navController.navigate(AppScreens.WelcomeScreen.route) {
                            // Limpia toda la pila de navegación hasta el inicio del grafo
                            // para que el usuario no pueda volver atrás con el botón de retroceso.
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true // Incluye el destino inicial en la limpieza
                            }
                            // Asegura que no se creen múltiples instancias de WelcomeScreen
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    // Usa el color 'error' definido en el tema para el contenedor del botón
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                        // El color del texto (onError) lo toma automáticamente del tema (usualmente blanco)
                    )
                ) {
                    // Icono dentro del botón
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null, // El texto del botón ya describe la acción
                        modifier = Modifier.padding(end = 8.dp) // Espacio entre icono y texto
                    )
                    // Texto del botón
                    Text(
                        "Cerrar Sesión",
                        style = MaterialTheme.typography.labelLarge // Estilo para texto de botones
                    )
                }
            }
        }
    }
}





