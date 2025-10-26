package com.example.goldenburgers.view



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// Quitamos import no usado
// import androidx.compose.ui.graphics.Brush
// import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
// Quitamos import no usado
// import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
// Quitamos import no usado
// import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.goldenburgers.R // Asegúrate de importar tu R
import com.example.goldenburgers.navigation.AppScreens


/**
 * Pantalla Composable de bienvenida, mostrada al iniciar la app si el usuario no está logueado.
 * @param navController Controlador de navegación para ir a Login o Registro.
 */
@Composable
fun WelcomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Usa el color de fondo definido en el tema.
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- OPCIONAL: Imagen de fondo ---
        // Descomenta y ajusta si tienes una imagen 'welcome_background' en drawable.
        /*
        Image(
            painter = painterResource(id = R.drawable.welcome_background),
            contentDescription = "Fondo de hamburguesas", // Descripción para accesibilidad
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop, // Escala la imagen para cubrir el fondo
            alpha = 0.3f // Le da opacidad para que no compita con el texto
        )
        */
        // --- FIN OPCIONAL ---


        // Columna principal que organiza el contenido verticalmente.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp), // Padding general
            horizontalAlignment = Alignment.CenterHorizontally, // Centra horizontalmente
        ) {
            // Empuja el contenido hacia abajo usando peso.
            Spacer(modifier = Modifier.weight(1f))


            // Título de la App
            Text(
                text = "Golden Burgers",
                // Usa estilo y color del tema para títulos grandes.
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground, // Color principal de texto
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(16.dp)) // Espacio


            // Eslogan de la App
            Text(
                text = "Una deliciosa hamburguesa es lo que te mereces aquí y ahora.",
                // Usa estilo y color secundario del tema.
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant, // Color para texto secundario
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(64.dp)) // Espacio mayor antes de los botones


            // Botón de Ingresar
            Button(
                onClick = { navController.navigate(AppScreens.LoginScreen.route) }, // Navega a Login
                modifier = Modifier
                    .fillMaxWidth() // Ocupa todo el ancho
                    .height(50.dp), // Altura fija
                shape = RoundedCornerShape(12.dp), // Bordes redondeados
                // Los colores por defecto (primary, onPrimary) son tomados del tema.
            ) {
                Text(
                    text = "Ingresar",
                    style = MaterialTheme.typography.labelLarge // Estilo para texto de botones
                )
            }


            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre botones


            // Botón de Registrar
            Button(
                onClick = { navController.navigate(AppScreens.RegisterStep1Screen.route) }, // Navega a Registro
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                // Usa colores secundarios/contenedores del tema para diferenciarlo.
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer, // Fondo del botón
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer // Color del texto
                )
            ) {
                Text(
                    text = "Registrar",
                    style = MaterialTheme.typography.labelLarge // Estilo para texto de botones
                )
            }
        }
    }
}






