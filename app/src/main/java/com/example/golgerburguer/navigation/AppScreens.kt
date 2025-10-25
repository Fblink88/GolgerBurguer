package com.example.golgerburguer.navigation

/**
 * Define las rutas de todas las pantallas de la aplicación como objetos sellados,
 * para evitar errores de tipeo y centralizar la gestión de rutas.
 */
sealed class AppScreens(val route: String) {
    // Pantallas del flujo de autenticación y registro
    object WelcomeScreen : AppScreens("welcome_screen")
    object LoginScreen : AppScreens("login_screen")
    object RegisterStep1Screen : AppScreens("register_step1_screen")
    object RegisterStep2Screen : AppScreens("register_step2_screen")
    object RegisterStep3Screen : AppScreens("register_step3_screen")
    object RegisterStep4Screen : AppScreens("register_step4_screen")
    object RegisterStep5Screen : AppScreens("register_step5_screen")

    // [NUEVO] Pantalla para editar el perfil de usuario
    object EditProfileScreen : AppScreens("edit_profile_screen")

    // Pantallas principales (dentro del BottomNav)
    object HomeScreen : AppScreens("home_screen")
    object FavoritesScreen : AppScreens("favorites_screen")
    object CartScreen : AppScreens("cart_screen")
    object ProfileScreen : AppScreens("profile_screen")
}
