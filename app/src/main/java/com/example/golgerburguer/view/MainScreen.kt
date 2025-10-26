package com.example.golgerburguer.view

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.golgerburguer.model.SessionManager
import com.example.golgerburguer.model.ThemeManager
import com.example.golgerburguer.navigation.AppScreens
import com.example.golgerburguer.navigation.BottomNavItem
import com.example.golgerburguer.viewmodel.CatalogViewModel

/**
 * [ACTUALIZADO] Añadida una TopAppBar principal que muestra el saludo al usuario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainNavController: NavController,
    sessionManager: SessionManager,
    themeManager: ThemeManager,
    catalogViewModel: CatalogViewModel
) {
    val bottomBarNavController = rememberNavController()
    val uiState by catalogViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            // [NUEVO] TopAppBar principal para todas las pantallas.
            TopAppBar(
                title = {
                    val userName = uiState.userName
                    if (userName != null) {
                        Text("Hola, ${userName.split(" ").first()}!")
                    } else {
                        Text("Golger Burguer")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController = bottomBarNavController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            BottomNavGraph(
                mainNavController = mainNavController,
                bottomBarNavController = bottomBarNavController,
                sessionManager = sessionManager,
                themeManager = themeManager,
                catalogViewModel = catalogViewModel
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(BottomNavItem.Home, BottomNavItem.Favorites, BottomNavItem.Cart, BottomNavItem.Profile)
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                label = { Text(text = item.title) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun BottomNavGraph(
    mainNavController: NavController,
    bottomBarNavController: NavHostController,
    sessionManager: SessionManager,
    themeManager: ThemeManager,
    catalogViewModel: CatalogViewModel
) {
    NavHost(
        navController = bottomBarNavController,
        startDestination = BottomNavItem.Home.route,
        enterTransition = { fadeIn(animationSpec = tween(200)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) }
    ) {
        composable(AppScreens.HomeScreen.route) {
            HomeScreen(catalogViewModel = catalogViewModel)
        }
        composable(AppScreens.FavoritesScreen.route) {
            FavoritesScreen(catalogViewModel = catalogViewModel)
        }
        composable(AppScreens.CartScreen.route) {
            CartScreen(catalogViewModel = catalogViewModel)
        }
        composable(AppScreens.ProfileScreen.route) {
            // La pantalla de perfil no tendrá la TopAppBar principal para mostrar su propio título.
            // Esto se maneja quitando la TopAppBar de la MainScreen y poniéndola individualmente
            // en las otras 3 pantallas, o usando una lógica más compleja para mostrar/ocultar
            // la TopAppBar principal dependiendo de la ruta actual.
            // Por simplicidad, por ahora la ProfileScreen también tendrá el saludo.
            ProfileScreen(
                navController = mainNavController,
                sessionManager = sessionManager,
                themeManager = themeManager,
                catalogViewModel = catalogViewModel
            )
        }
    }
}
