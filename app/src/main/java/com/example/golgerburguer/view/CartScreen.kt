package com.example.golgerburguer.view




import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
// Quita import de Color si no lo usas directamente
// import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.golgerburguer.viewmodel.CartItem
import com.example.golgerburguer.viewmodel.CatalogViewModel
import com.example.golgerburguer.viewmodel.toCurrencyFormat


/**
 * Pantalla Composable que muestra el contenido del carrito de compras.
 * @param catalogViewModel ViewModel que contiene el estado del catálogo y el carrito.
 */
@Composable
fun CartScreen(catalogViewModel: CatalogViewModel) { // Recibe el ViewModel
    val uiState by catalogViewModel.uiState.collectAsState()
    val cartItems = uiState.cartItems


    Scaffold(
        // Muestra el resumen del carrito en la parte inferior solo si hay items
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                CartSummary(
                    subtotal = uiState.cartSubtotal,
                    onCheckoutClick = { /* TODO: Implementar lógica de navegación a pantalla de pago */ }
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Aplica el padding del Scaffold
            color = MaterialTheme.colorScheme.background // Usa color de fondo del tema
        ) {
            // Muestra un mensaje si el carrito está vacío, o la lista si tiene items
            if (cartItems.isEmpty()) {
                EmptyCartMessage()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp), // Espaciado interno de la lista
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre items
                ) {
                    items(cartItems, key = { it.product.id }) { cartItem -> // key ayuda a optimizar recomposiciones
                        CartItemCard(
                            cartItem = cartItem,
                            onIncrease = { catalogViewModel.increaseQuantity(cartItem.product.id) },
                            onDecrease = { catalogViewModel.decreaseQuantity(cartItem.product.id) }
                        )
                    }
                }
            }
        }
    }
}


/**
 * Composable que representa la tarjeta de un item individual en el carrito.
 */
@Composable
fun CartItemCard(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Color de tarjeta del tema
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = cartItem.product.imagenReferencia),
                contentDescription = cartItem.product.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    cartItem.product.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface // Color de texto principal
                )
                Text(
                    cartItem.product.precio.toCurrencyFormat(),
                    color = MaterialTheme.colorScheme.primary, // Color primario para precio
                    fontWeight = FontWeight.SemiBold
                )
            }
            // Controles para aumentar/disminuir cantidad
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val buttonBorderColor = MaterialTheme.colorScheme.outline // Color de borde del tema
                IconButton(
                    onClick = onDecrease,
                    modifier = Modifier
                        .size(32.dp)
                        .border(1.dp, buttonBorderColor, RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Quitar uno") // Descripción para accesibilidad
                }
                Text(
                    cartItem.quantity.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier
                        .size(32.dp)
                        .border(1.dp, buttonBorderColor, RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir uno") // Descripción para accesibilidad
                }
            }
        }
    }
}


/**
 * Composable que muestra el resumen del carrito (subtotal, propina, total) y el botón de pagar.
 */
@Composable
fun CartSummary(subtotal: Double, onCheckoutClick: () -> Unit) {
    val tip = subtotal * 0.10 // Calcula 10% de propina
    val total = subtotal + tip


    Surface(
        shadowElevation = 8.dp, // Sombra para destacar el resumen
        color = MaterialTheme.colorScheme.surface // Color de fondo del resumen
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Fila para el costo de los productos
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Costo productos",
                    style = MaterialTheme.typography.bodyLarge, // Estilo del tema
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Color secundario
                )
                Text(
                    subtotal.toCurrencyFormat(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface // Color principal
                )
            }
            Spacer(Modifier.height(8.dp))
            // Fila para la propina
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Propina",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    tip.toCurrencyFormat(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Divider(modifier = Modifier.padding(vertical = 16.dp)) // Separador con color del tema
            // Fila para el Total
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Total",
                    style = MaterialTheme.typography.titleMedium, // Estilo más grande para total
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    total.toCurrencyFormat(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(16.dp))
            // Botón de Pagar
            Button(
                onClick = onCheckoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                // Colores por defecto del tema (primary, onPrimary)
            ) {
                Text(
                    "Pagar",
                    style = MaterialTheme.typography.labelLarge // Estilo para texto de botones
                )
            }
        }
    }
}


/**
 * Composable que se muestra cuando el carrito está vacío.
 */
@Composable
fun EmptyCartMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = "Carrito vacío", // Descripción para accesibilidad
                modifier = Modifier.size(80.dp),
                // Color suave del tema para el icono
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Tu carrito está vacío",
                style = MaterialTheme.typography.headlineSmall, // Estilo del tema
                color = MaterialTheme.colorScheme.onBackground // Color principal
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Añade algunas deliciosas hamburguesas para empezar.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
                style = MaterialTheme.typography.bodyLarge, // Estilo del tema
                color = MaterialTheme.colorScheme.onSurfaceVariant // Color secundario
            )
        }
    }
}





