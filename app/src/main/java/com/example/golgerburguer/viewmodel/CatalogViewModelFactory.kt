package com.example.golgerburguer.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.golgerburguer.model.ProductRepository


/**
 * Fábrica personalizada para crear instancias de CatalogViewModel.
 * Es necesaria porque CatalogViewModel ahora tiene una dependencia (ProductRepository)
 * que debe ser inyectada en su constructor.
 *
 * @param repository La instancia del ProductRepository que se pasará al ViewModel.
 */
class CatalogViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {


    /**
     * Método llamado por el sistema para crear una instancia del ViewModel solicitado.
     * @param modelClass La clase del ViewModel que se necesita crear.
     * @return Una instancia del ViewModel solicitado (en este caso, CatalogViewModel).
     * @throws IllegalArgumentException si la clase solicitada no es CatalogViewModel.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica si la clase solicitada es CatalogViewModel o una subclase de ella.
        if (modelClass.isAssignableFrom(CatalogViewModel::class.java)) {
            // Si es así, crea una instancia de CatalogViewModel pasándole el repositorio.
            // Usa @Suppress("UNCHECKED_CAST") porque estamos seguros del tipo.
            @Suppress("UNCHECKED_CAST")
            return CatalogViewModel(repository) as T
        }
        // Si se solicita crear un ViewModel desconocido, lanza una excepción.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}





