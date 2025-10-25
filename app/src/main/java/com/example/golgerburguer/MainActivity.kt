package com.example.golgerburguer // Asegúrate que el paquete coincida


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.golgerburguer.model.GolgerBurguerDatabase
import com.example.golgerburguer.model.ProductRepository
import com.example.golgerburguer.data.SessionManager
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
                    // --- Configuración de la Inyección de Dependencias Manual ---
                    // Obtiene el contexto actual dentro del Composable.
                    val context = LocalContext.current
                    // Obtiene/Crea la instancia singleton de la base de datos Room.
                    // 'remember' asegura que no se recree en cada recomposición.
                    val db = remember { GolgerBurguerDatabase.getDatabase(context) }
                    // Crea la instancia del Repositorio, pasándole el DAO de la base de datos.
                    val repository = remember { ProductRepository(db.productDao()) }
                    // Crea la instancia del CatalogViewModel utilizando la Factory
                    // para inyectar el repositorio.
                    val catalogViewModel: CatalogViewModel = viewModel(
                        factory = CatalogViewModelFactory(repository)
                    )
                    // --- Fin de la Configuración ---


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





