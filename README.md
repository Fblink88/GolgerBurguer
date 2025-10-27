1.⁠ ⁠Configuración Inicial del Proyecto y UI Base
•Configuración de Gradle: Se configuraron los archivos build.gradle.kts y gradle/libs.versions.toml con todas las dependencias necesarias para Jetpack Compose, Navigation, Room, DataStore y Coil.
•Creación del Tema con Material 3: Se definió la paleta de colores (Color.kt), la tipografía (Type.kt) y el tema principal de la aplicación (Theme.kt), estableciendo las bases del diseño visual.
•Estructura de Carpetas (Arquitectura): Se organizó el proyecto en paquetes (navigation, view, viewmodel, model), sentando las bases para una arquitectura MVVM limpia y escalable.

2.⁠ ⁠Sistema de Navegación (Jetpack Compose Navigation)
•Definición de Rutas: Se crearon todas las rutas de la aplicación de forma segura usando una clase sellada (AppScreens.kt).
•Navegación Principal: Se implementó un NavHost principal en AppNavigation.kt que gestiona el flujo inicial: si el usuario no ha iniciado sesión, lo lleva a WelcomeScreen; si ya ha iniciado sesión, lo lleva al flujo principal (main_flow).
•Navegación con Menú Inferior: Se creó el BottomNavigationBar con sus iconos y rutas (BottomNavItem.kt) y se implementó un NavHost anidado dentro de MainScreen.kt para gestionar la navegación entre "Catálogo", "Favoritos", "Carrito" y "Perfil".


3.⁠ ⁠Base de Datos y Persistencia de Datos
•Modelado de Datos: Se crearon las clases Producto y User que actúan como entidades (@Entity) para la base de datos Room.
•Implementación de Room:
◦Se crearon los DAOs (ProductDao, UserDao) con las consultas para acceder y modificar los datos.
◦Se implementó la base de datos principal (GolgerBurguerDatabase.kt) y la lógica para poblar la tabla de productos con datos iniciales la primera vez que se crea.
•Patrón Repositorio: Se creó el ProductRepository para centralizar el acceso a los datos, actuando como intermediario entre los ViewModels y los DAOs.


4.⁠ ⁠Flujo de Autenticación de Usuarios
•Formularios de Registro (Multi-paso): Se crearon 5 pantallas para un registro guiado, recopilando credenciales, datos personales, dirección y datos opcionales.
•Lógica y Validación en ViewModel: Se implementó el RegisterViewModel, que contiene toda la lógica para validar cada campo del formulario en tiempo real (email, contraseña, teléfono, etc.) y mostrar mensajes de error, manteniendo la UI limpia de lógica.
•Login de Usuario: Se creó la pantalla de LoginScreen y su LoginViewModel, que se comunica con el Repository para verificar las credenciales del usuario contra la base de datos.
•Gestión de Sesión con DataStore: Se creó el SessionManager para guardar el email del usuario de forma persistente, permitiendo que la app "recuerde" al usuario si cierra y vuelve a abrir la aplicación.

5.⁠ ⁠Funcionalidades Principales de la Aplicación
•Catálogo de Productos (HomeScreen): Pantalla principal que muestra todos los productos en una cuadrícula (LazyVerticalGrid).
•Carrito de Compras (CartScreen):
◦Lógica en CatalogViewModel para añadir, incrementar, decrementar y eliminar productos del carrito.
◦El carrito se limpia automáticamente al cerrar sesión.
•Lista de Favoritos (FavoritesScreen): Implementación de la lógica para marcar/desmarcar productos como favoritos (toggleFavorite) y una pantalla que muestra únicamente los productos marcados.


6.⁠ ⁠Perfil de Usuario y Preferencias
•Edición de Perfil:
◦Se creó la pantalla EditProfileScreen que se rellena automáticamente con los datos del usuario logueado.
◦Se implementó el EditProfileViewModel con la lógica para cargar los datos del usuario actual y guardarlos en la base de datos al pulsar "Guardar Cambios".•Modo Oscuro Persistente:
◦Se creó el ThemeManager usando DataStore para guardar la preferencia de tema del usuario.
◦Se conectó el tema principal de la app a este Manager para que el cambio entre modo claro y oscuro sea dinámico y se recuerde entre sesiones.

7.⁠ ⁠Integración de Recursos Nativos del Dispositivo
•Cámara y Galería de Fotos:
◦Se configuró el AndroidManifest.xml con los permisos de cámara y el FileProvider para un manejo seguro de archivos.
◦Se implementó la lógica con ActivityResultLauncher para pedir el permiso de cámara en tiempo de ejecución, y para abrir la cámara o la galería y recibir la imagen seleccionada.
•GPS para Autocompletar Dirección:
◦Se añadió la dependencia de los servicios de ubicación de Google.
◦Se implementó la lógica en RegisterStep2Screen para pedir permiso de ubicación y, si se concede, usar el FusedLocationProviderClient y el Geocoder para obtener la dirección actual y rellenar los campos automáticamente.¿Qué tareas clave se te podrían estar olvidando?Tu lista era muy buena, pero estas son las tareas "extra" y de arquitectura que también habéis completado y que son muy importantes:
•Implementación de una arquitectura MVVM limpia.
•Creación de un sistema de navegación robusto con un flujo de autenticación separado del principal.
•Implementación del Patrón Repositorio para el acceso a datos.
•Uso de ViewModelFactory para inyectar dependencias en los ViewModels.•Gestión de preferencias del usuario (como el Modo Oscuro) usando DataStore.
•Implementación de un flujo de registro multi-paso.
