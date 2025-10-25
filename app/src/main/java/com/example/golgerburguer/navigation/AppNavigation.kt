package com.example.golgerburguer.navigation // Asegúrate que el paquete coincida


import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.golgerburguer.model.SessionManager
import com.example.golgerburguer.view.*
import com.example.golgerburguer.viewmodel.CatalogViewModel
import com.example.golgerburguer.viewmodel.LoginViewModel
import com.example.golgerburguer.viewmodel.RegisterViewModel



/**
 * Define el grafo de navegación principal de la aplicación.
 * Gestiona el flujo entre autenticación, registro y la pantalla principal.
 * @param sessionManager Instancia para gestionar el estado de la sesión.
 * @param catalogViewModel Instancia del ViewModel principal (catálogo, favs, carrito), creado en MainActivity.
 */
@Composable
fun AppNavigation(
    sessionManager: SessionManager,
    catalogViewModel: CatalogViewModel // Recibimos el ViewModel principal
) {
    val navController = rememberNavController()
    // ViewModels que se usan solo en partes específicas del grafo
    val registerViewModel: RegisterViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()


    // Observa el estado de la sesión para determinar la pantalla inicial
    val isLoggedIn by sessionManager.isLoggedInFlow.collectAsState(initial = null)


    // Muestra un indicador de carga mientras se lee el estado de la sesión desde DataStore
    if (isLoggedIn == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return // Sale temprano hasta que tengamos el estado
    }


    // Determina la ruta de inicio basada en si el usuario está logueado
    val startDestination = if (isLoggedIn == true) {
        "main_flow" // Ruta para la pantalla principal (con BottomNav)
    } else {
        AppScreens.WelcomeScreen.route // Ruta para la pantalla de bienvenida
    }


    // Configura el NavHost principal
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // --- Definición de las transiciones ---
        val slideDuration = 300 // Duración en milisegundos
        val slideIn = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(slideDuration))
        val slideOut = slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(slideDuration))
        val popIn = slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(slideDuration))
        val popOut = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(slideDuration))
        val fadeDuration = 300
        val fadeInSpec = fadeIn(animationSpec = tween(fadeDuration))
        val fadeOutSpec = fadeOut(animationSpec = tween(fadeDuration))


        // --- Rutas del Flujo de Autenticación/Registro ---


        composable(
            AppScreens.WelcomeScreen.route,
            enterTransition = { fadeInSpec },
            exitTransition = { fadeOutSpec }
        ) { WelcomeScreen(navController) }


        composable(
            AppScreens.LoginScreen.route,
            enterTransition = { slideIn }, exitTransition = { slideOut },
            popEnterTransition = { popIn }, popExitTransition = { popOut }
        ) {
            // Pasa el LoginViewModel a la pantalla de Login
            LoginScreen(
                navController = navController,
                sessionManager = sessionManager,
                loginViewModel = loginViewModel
            )
        }


        composable(
            AppScreens.RegisterStep1Screen.route,
            enterTransition = { slideIn }, exitTransition = { slideOut },
            popEnterTransition = { popIn }, popExitTransition = { popOut }
        ) { RegisterStep1Screen(navController, registerViewModel) }


        composable(
            AppScreens.RegisterStep2Screen.route,
            enterTransition = { slideIn }, exitTransition = { slideOut },
            popEnterTransition = { popIn }, popExitTransition = { popOut }
        ) { RegisterStep2Screen(navController, registerViewModel) }


        composable(
            AppScreens.RegisterStep3Screen.route,
            enterTransition = { slideIn }, exitTransition = { slideOut },
            popEnterTransition = { popIn }, popExitTransition = { popOut }
        ) { RegisterStep3Screen(navController, registerViewModel) }


        composable(
            AppScreens.RegisterStep4Screen.route,
            enterTransition = { slideIn }, exitTransition = { slideOut },
            popEnterTransition = { popIn }, popExitTransition = { popOut }
        ) { RegisterStep4Screen(navController, registerViewModel) }


        composable(
            AppScreens.RegisterStep5Screen.route,
            enterTransition = { slideIn }, exitTransition = { slideOut },
            popEnterTransition = { popIn }, popExitTransition = { popOut }
        ) { RegisterStep5Screen(navController, registerViewModel, sessionManager) }


        // --- Ruta para el Flujo Principal (App Logueada) ---


        composable(
            route = "main_flow", // Ruta que representa toda la app principal con BottomNav
            enterTransition = { fadeIn(animationSpec = tween(500)) }, // Fundido más lento al entrar a la app
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            // Carga MainScreen, pasándole el NavController principal y los managers necesarios
            MainScreen(
                mainNavController = navController, // Para acciones como cerrar sesión y volver a Welcome
                sessionManager = sessionManager,
                catalogViewModel = catalogViewModel // Pasa el ViewModel principal
            )
        }
    }
}
