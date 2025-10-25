package com.example.golgerburguer.navigation // Asegúrate que el paquete coincida


/**
 * Sealed class que define todas las rutas de navegación únicas en la aplicación.
 * Cada objeto representa una pantalla o destino específico.
 * Usar objetos dentro de una sealed class asegura que solo estas rutas predefinidas
 * puedan ser utilizadas, previniendo errores tipográficos al navegar.
 */
sealed class AppScreens(val route: String) { // Cada pantalla tiene una 'route' (String) asociada


    // --- Flujo de Autenticación/Registro ---
    object WelcomeScreen : AppScreens("welcome_screen") // Pantalla de bienvenida inicial
    object LoginScreen : AppScreens("login_screen")     // Pantalla de inicio de sesión
    object RegisterStep1Screen : AppScreens("register_step1_screen") // Registro - Paso 1 (Credenciales)
    object RegisterStep2Screen : AppScreens("register_step2_screen") // Registro - Paso 2 (Nombre)
    object RegisterStep3Screen : AppScreens("register_step3_screen") // Registro - Paso 3 (Foto)
    object RegisterStep4Screen : AppScreens("register_step4_screen") // Registro - Paso 4 (Dirección)
    object RegisterStep5Screen : AppScreens("register_step5_screen") // Registro - Paso 5 (Éxito)


    // --- Flujo Principal (Dentro del BottomNavigationBar) ---
    // Nota: Aunque estas rutas se usan en el NavHost anidado de MainScreen,
    // también las definimos aquí para consistencia y posible uso futuro.
    object HomeScreen : AppScreens("home_screen")         // Pantalla principal (Catálogo)
    object FavoritesScreen : AppScreens("favorites_screen") // Pantalla de favoritos
    object CartScreen : AppScreens("cart_screen")         // Pantalla del carrito
    object ProfileScreen : AppScreens("profile_screen")     // Pantalla de perfil de usuario


    // Podrías añadir más rutas aquí si tu app crece, por ejemplo:
    // object ProductDetailScreen : AppScreens("product_detail_screen/{productId}") // Pantalla de detalle con argumento
    // object OrderHistoryScreen : AppScreens("order_history_screen") // Historial de pedidos
}





