package com.example.golgerburguer.view




import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color // Mantenemos Color para el .copy(alpha) si es necesario (ej. icono fav sobre imagen)
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Eliminamos import innecesarios de TextOverflow si no se usa
import com.example.golgerburguer.model.Producto
import com.example.golgerburguer.viewmodel.CatalogViewModel


/**
 * Pantalla Composable principal que muestra el catálogo de productos.
 * @param catalogViewModel ViewModel que contiene el estado del catálogo.
 */
@Composable
fun HomeScreen(catalogViewModel: CatalogViewModel) { // Recibe el ViewModel
    // Observamos el estado del ViewModel. uiState.products ahora viene de la BD.
    val uiState by catalogViewModel.uiState.collectAsState()


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background // Usa color de fondo del tema
    ) {
        // LazyColumn para mostrar eficientemente la lista de productos.
        LazyColumn(
            contentPadding = PaddingValues(16.dp), // Espaciado interno
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre tarjetas
        ) {
            // Itera sobre la lista de productos del estado del ViewModel.
            items(uiState.products, key = { it.id }) { product -> // Usa el id como key para optimización
                // Llama al Composable ProductCard para cada producto.
                ProductCard(
                    product = product,
                    // Pasa la lambda para manejar el clic en el icono de favorito.
                    onFavoriteClick = {
                        catalogViewModel.toggleFavorite(product.id, product.esFavorito)
                    },
                    // Pasa la lambda para manejar el clic en el botón de añadir al carrito.
                    onAddToCartClick = { catalogViewModel.addToCart(product) }
                )
            }
        }
    }
}


/**
 * Composable reutilizable que muestra la tarjeta de un producto individual.
 * @param product El objeto Producto a mostrar.
 * @param onFavoriteClick Lambda que se ejecuta al hacer clic en el icono de favorito.
 * @param onAddToCartClick Lambda que se ejecuta al hacer clic en el botón "Añadir".
 */
@Composable
fun ProductCard(
    product: Producto,
    onFavoriteClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        // Usamos el color de superficie definido en el tema para el fondo de la tarjeta.
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // Contenedor para la imagen y el icono de favorito superpuesto.
            Box(modifier = Modifier.height(140.dp)) {
                Image(
                    painter = painterResource(id = product.imagenReferencia),
                    contentDescription = product.nombre, // Descripción para accesibilidad
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // Escala la imagen para llenar el espacio
                )
                // Botón de icono para marcar/desmarcar favorito.
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd) // Lo alinea arriba a la derecha
                        .padding(8.dp) // Añade un pequeño padding
                ) {
                    Icon(
                        imageVector = if (product.esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Marcar como favorito", // Descripción para accesibilidad
                        // Usa el color de error del tema si es favorito, blanco si no lo es
                        // (Blanco contrasta bien sobre la mayoría de las imágenes).
                        tint = if (product.esFavorito) MaterialTheme.colorScheme.error else Color.White
                    )
                }
            }
            // Fila inferior con el nombre, precio y botón de añadir.
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically // Centra verticalmente los elementos
            ) {
                // Columna para el nombre y el precio, ocupa el espacio restante.
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        product.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        // Usa el color principal de texto sobre la superficie del tema.
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1 // Evita que el nombre ocupe múltiples líneas
                    )
                    Text(
                        text = "$${product.precio.toInt()}", // Formato simple de precio
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        // Usa el color primario del tema para destacar el precio.
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                // Botón para añadir al carrito.
                Button(
                    onClick = onAddToCartClick
                    // Los colores del botón (contenedor y contenido) los toma automáticamente del tema.
                ) {
                    Text(
                        "Añadir",
                        style = MaterialTheme.typography.labelLarge // Usa el estilo de texto para botones del tema
                    )
                }
            }
        }
    }
}





