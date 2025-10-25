package com.example.golgerburguer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.golgerburguer.model.GolgerBurguerDatabase
import com.example.golgerburguer.model.ProductRepository
import com.example.golgerburguer.model.SessionManager
import com.example.golgerburguer.model.ThemeManager
import com.example.golgerburguer.navigation.AppNavigation
import com.example.golgerburguer.ui.theme.GolgerBurguerTheme
import com.example.golgerburguer.viewmodel.CatalogViewModel
import com.example.golgerburguer.viewmodel.CatalogViewModelFactory

/**
 * La actividad principal y único punto de entrada de la aplicación.
 */
class MainActivity : ComponentActivity() {

    private val sessionManager by lazy { SessionManager(this) }
    private val themeManager by lazy { ThemeManager(this) }

    private val catalogViewModel: CatalogViewModel by viewModels {
        val database = GolgerBurguerDatabase.getDatabase(this)
        val repository = ProductRepository(database.productDao(), database.userDao())
        CatalogViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // [ACTUALIZADO] Lee el estado del modo oscuro desde el ThemeManager.
            val isDarkMode by themeManager.isDarkMode.collectAsState(initial = false)

            // Pasa el estado al parámetro `darkTheme` de nuestro tema personalizado.
            GolgerBurguerTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        sessionManager = sessionManager,
                        themeManager = themeManager,
                        catalogViewModel = catalogViewModel
                    )
                }
            }
        }
    }
}