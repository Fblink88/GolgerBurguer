package com.example.golgerburguer.model




import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.golgerburguer.data.ProductDao


/**
 * Clase que representa la base de datos Room de la aplicación.
 * Define las entidades que contiene y la versión del esquema.
 */
@Database(entities = [Producto::class], version = 1, exportSchema = false)
abstract class GolgerBurguerDatabase : RoomDatabase() {


    // Método abstracto que Room implementará para devolver el DAO
    abstract fun productDao(): ProductDao


    companion object {
        // La instancia única de la base de datos.
        // Volatile asegura que los cambios sean visibles inmediatamente para todos los hilos.
        @Volatile
        private var INSTANCE: GolgerBurguerDatabase? = null


        /**
         * Obtiene la instancia única (singleton) de la base de datos.
         * Utiliza un bloque synchronized para evitar condiciones de carrera al crear la instancia.
         * @param context El contexto de la aplicación.
         * @return La instancia única de GolgerBurguerDatabase.
         */
        fun getDatabase(context: Context): GolgerBurguerDatabase {
            // Si la instancia ya existe, la devuelve.
            // Si no, crea la base de datos de forma segura.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GolgerBurguerDatabase::class.java,
                    "golgerburguer_database" // Nombre del archivo de la base de datos
                )
                    // Estrategia de migración: Si cambias la versión, borra y recrea la BD.
                    // Para producción, se usarían migraciones reales.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // Devuelve la nueva instancia
                instance
            }
        }
    }
}





