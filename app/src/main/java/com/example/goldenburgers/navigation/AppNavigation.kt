package com.example.goldenburgers.navigation

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
import com.example.goldenburgers.view.EditProfileScreen
import com.example.goldenburgers.model.SessionManager
import com.example.goldenburgers.model.ThemeManager
import com.example.goldenburgers.view.*
import com.example.goldenburgers.viewmodel.*

/**
 * [ACTUALIZADO] Corregido el bucle de carga infinito al inicio de la app.
 */
@Composable
fun AppNavigation(
    sessionManager: SessionManager,
    themeManager: ThemeManager,
    catalogViewModel: CatalogViewModel
) {
    val navController = rememberNavController()

    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(catalogViewModel.repository))
    val registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(catalogViewModel.repository))
    val editProfileViewModel: com.example.goldenburgers.viewmodel.EditProfileViewModel = viewModel(factory = EditProfileViewModelFactory(catalogViewModel.repository, sessionManager))

    // [CORREGIDO] Se usa un String vacío como estado inicial para diferenciar "cargando" de "sin sesión" (null).
    val loggedInUserEmail by sessionManager.loggedInUserEmailFlow.collectAsState(initial = "")

    // La carga solo ocurre mientras el estado sea el inicial (String vacío).
    val isLoadingSession = loggedInUserEmail == ""

    if (isLoadingSession) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    // La ruta inicial ahora depende de si el email es un String con contenido.
    val startDestination = if (!loggedInUserEmail.isNullOrBlank()) {
        "main_flow"
    } else {
        AppScreens.WelcomeScreen.route
    }

    NavHost(navController = navController, startDestination = startDestination) {
        val slideDuration = 300
        val slideIn = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(slideDuration))
        val slideOut = slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(slideDuration))
        val popIn = slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(slideDuration))
        val popOut = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(slideDuration))

        composable(AppScreens.WelcomeScreen.route) { WelcomeScreen(navController) }
        composable(AppScreens.LoginScreen.route, enterTransition = { slideIn }, exitTransition = { slideOut }) {
            LoginScreen(navController, sessionManager, loginViewModel)
        }

        composable(AppScreens.RegisterStep1Screen.route) { RegisterStep1Screen(navController, registerViewModel) }
        composable(AppScreens.RegisterStep2Screen.route) { RegisterStep2Screen(navController, registerViewModel) }
        composable(AppScreens.RegisterStep3Screen.route) { RegisterStep3Screen(navController, registerViewModel) }
        composable(AppScreens.RegisterStep4Screen.route) { RegisterStep4Screen(navController, registerViewModel) }
        composable(AppScreens.RegisterStep5Screen.route) { RegisterStep5Screen(navController, registerViewModel, sessionManager) }

        composable(AppScreens.EditProfileScreen.route, enterTransition = { slideIn }, exitTransition = { slideOut }) {
            EditProfileScreen(navController = navController, viewModel = editProfileViewModel)
        }

        composable("main_flow", enterTransition = { fadeIn(animationSpec = tween(500)) }) {
            MainScreen(mainNavController = navController, sessionManager = sessionManager, themeManager = themeManager, catalogViewModel = catalogViewModel)
        }
    }
}

