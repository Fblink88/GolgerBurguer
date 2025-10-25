package com.example.golgerburguer.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.golgerburguer.model.ProductDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Clase que representa la base de datos Room de la aplicación.
 * Define las entidades que contiene y la versión del esquema.
 */
@Database(entities = [Producto::class], version = 1, exportSchema = false)
abstract class GolgerBurguerDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: GolgerBurguerDatabase? = null

        fun getDatabase(context: Context): GolgerBurguerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GolgerBurguerDatabase::class.java,
                    "golgerburguer_database"
                )
                    .fallbackToDestructiveMigration()
                    // Añade el callback para poblar la base de datos en su creación.
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Callback que se ejecuta cuando la base de datos se crea por primera vez.
     * Es el lugar ideal para poblar la base de datos con datos iniciales.
     */
    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                database ->
                // Lanza una corutina para insertar los datos iniciales en un hilo secundario.
                CoroutineScope(Dispatchers.IO).launch {
                    val productDao = database.productDao()
                    productDao.insertAll(FakeProductDataSource.products)
                }
            }
        }
    }
}
