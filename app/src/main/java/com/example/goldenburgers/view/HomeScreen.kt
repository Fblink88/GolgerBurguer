package com.example.goldenburgers.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.goldenburgers.model.Producto
import com.example.goldenburgers.viewmodel.CatalogViewModel
import com.example.goldenburgers.viewmodel.toCurrencyFormat

/**
 * [CORREGIDO] Se elimina la función duplicada de EditProfileScreen.
 */
@Composable
fun HomeScreen(catalogViewModel: CatalogViewModel) {
    val uiState by catalogViewModel.uiState.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(uiState.products) { product ->
            ProductCard(product = product, viewModel = catalogViewModel)
        }
    }
}

@Composable
fun ProductCard(product: Producto, viewModel: CatalogViewModel) {
    val context = LocalContext.current
    val imageResId = remember(product.imagenReferencia) {
        context.resources.getIdentifier(product.imagenReferencia.toString(), "drawable", context.packageName)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(modifier = Modifier.height(150.dp)) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = product.nombre,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { viewModel.toggleFavorite(product.id, product.esFavorito) },
                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (product.esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (product.esFavorito) Color.Red else Color.White
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(product.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(product.descripcion, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(product.precio.toCurrencyFormat(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Button(onClick = { viewModel.addToCart(product) }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}
