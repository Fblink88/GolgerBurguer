package com.example.golgerburguer // Asegúrate que el paquete coincida

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.golgerburguer.model.GolgerBurguerDatabase
import com.example.golgerburguer.model.ProductRepository
import com.example.golgerburguer.model.SessionManager
import com.example.golgerburguer.navigation.AppNavigation
import com.example.golgerburguer.ui.theme.GolgerBurguerTheme
import com.example.golgerburguer.viewmodel.CatalogViewModel
import com.example.golgerburguer.viewmodel.CatalogViewModelFactory

/**
 * La actividad principal y único punto de entrada de la aplicación Golger Burguer.
 */
class MainActivity : ComponentActivity() {

    // Instancia lazy del SessionManager, se crea solo la primera vez que se necesita.
    private val sessionManager by lazy {
        SessionManager(this)
    }

    // Inyecta el ViewModel usando la factory personalizada.
    // Esto asegura que el ViewModel sobreviva a cambios de configuración.
    private val catalogViewModel: CatalogViewModel by viewModels {
        val database = GolgerBurguerDatabase.getDatabase(this)
        val repository = ProductRepository(database.productDao())
        CatalogViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita el dibujo de borde a borde (edge-to-edge) para usar toda la pantalla.
        enableEdgeToEdge()
        setContent {
            // Aplica el tema de Material 3 personalizado (GolgerBurguerTheme).
            GolgerBurguerTheme {
                // Surface principal que ocupa toda la pantalla.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background // Color de fondo del tema
                ) {
                    // Lanza el sistema de navegación principal, pasando las dependencias necesarias.
                    AppNavigation(
                        sessionManager = sessionManager,
                        catalogViewModel = catalogViewModel
                    )
                }
            }
        }
    }
}