package com.example.golgerburguer.view




import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
// Quitamos import de viewModel() si ya no se usa aquí directamente
// import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.golgerburguer.model.SessionManager
import com.example.golgerburguer.navigation.AppScreens
import com.example.golgerburguer.navigation.BottomNavItem
import com.example.golgerburguer.viewmodel.CatalogViewModel


/**
 * Pantalla Composable principal que contiene el Scaffold con el BottomNavigationBar
 * y el NavHost anidado para las secciones principales de la app.
 * @param mainNavController El NavController principal de la app (de AppNavigation).
 * @param sessionManager El gestor de sesión.
 * @param catalogViewModel El ViewModel principal compartido (creado en MainActivity).
 */
@Composable
fun MainScreen(
    mainNavController: NavController,
    sessionManager: SessionManager,
    catalogViewModel: CatalogViewModel // Recibe el ViewModel
) {
    // NavController específico para la navegación DENTRO del BottomNavigationBar.
    val bottomBarNavController = rememberNavController()


    Scaffold(
        // Define la barra de navegación inferior.
        bottomBar = { BottomNavigationBar(navController = bottomBarNavController) }
    ) { innerPadding ->
        // Contenedor para el contenido principal, aplicando el padding del Scaffold.
        Box(modifier = Modifier.padding(innerPadding)) {
            // Llama al NavHost anidado que gestiona las pantallas del BottomBar.
            BottomNavGraph(
                mainNavController = mainNavController, // Pasa el NavController principal
                bottomBarNavController = bottomBarNavController, // Pasa el NavController del BottomBar
                sessionManager = sessionManager, // Pasa el SessionManager
                catalogViewModel = catalogViewModel // Pasa el CatalogViewModel
            )
        }
    }
}


/**
 * Composable que define la barra de navegación inferior (BottomNavigationBar).
 * @param navController El NavController que controla la navegación DENTRO de las pestañas.
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    // Lista de los items a mostrar en la barra, definidos en BottomNavItem.kt.
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Cart,
        BottomNavItem.Profile
    )


    NavigationBar { // Componente de Material 3 para la barra inferior
        // Obtiene el estado actual de la pila de navegación para saber qué pestaña está activa.
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route


        // Itera sobre cada item definido para crear su botón en la barra.
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route, // Marca como seleccionada si la ruta actual coincide
                label = { Text(text = item.title) }, // Muestra el título debajo del icono
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) }, // Muestra el icono
                onClick = {
                    // Acción al hacer clic en un item: navega a la ruta asociada.
                    navController.navigate(item.route) {
                        // Optimización: Evita acumular historial si se vuelve a la pestaña de inicio.
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true // Guarda el estado de la pantalla al salir
                        }
                        launchSingleTop = true // Evita crear múltiples instancias de la misma pantalla
                        restoreState = true // Restaura el estado al volver a la pantalla
                    }
                }
                // Los colores de los items seleccionados/no seleccionados los toma del tema.
            )
        }
    }
}


/**
 * Define el NavHost anidado que gestiona las pantallas accesibles desde el BottomNavigationBar.
 * @param mainNavController NavController principal (para acciones como cerrar sesión).
 * @param bottomBarNavController NavController para la navegación entre pestañas.
 * @param sessionManager Gestor de sesión (necesario para ProfileScreen).
 * @param catalogViewModel ViewModel principal (necesario para Home, Favorites, Cart).
 */
@Composable
fun BottomNavGraph(
    mainNavController: NavController,
    bottomBarNavController: NavHostController,
    sessionManager: SessionManager,
    catalogViewModel: CatalogViewModel // Recibe el ViewModel
) {
    // Configura el NavHost que cambiará el contenido principal según la pestaña seleccionada.
    NavHost(
        navController = bottomBarNavController, // Usa el NavController del BottomBar
        startDestination = BottomNavItem.Home.route, // La pestaña inicial es "Inicio"
        // Aplica transiciones de fundido suave al cambiar entre pestañas.
        enterTransition = { fadeIn(animationSpec = tween(200)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) }
    ) {
        // Define qué Composable mostrar para cada ruta del BottomNav.
        composable(AppScreens.HomeScreen.route) {
            HomeScreen(catalogViewModel = catalogViewModel) // Pasa el ViewModel
        }
        composable(AppScreens.FavoritesScreen.route) {
            FavoritesScreen(catalogViewModel = catalogViewModel) // Pasa el ViewModel
        }
        composable(AppScreens.CartScreen.route) {
            CartScreen(catalogViewModel = catalogViewModel) // Pasa el ViewModel
        }
        composable(AppScreens.ProfileScreen.route) {
            // ProfileScreen necesita el NavController principal para poder cerrar sesión
            // y volver a la pantalla de bienvenida.
            ProfileScreen(navController = mainNavController, sessionManager = sessionManager)
        }
    }
}





