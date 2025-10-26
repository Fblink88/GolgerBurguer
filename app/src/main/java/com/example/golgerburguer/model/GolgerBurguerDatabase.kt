package com.example.golgerburguer.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * [ACTUALIZADO] Corregido el uso obsoleto de fallbackToDestructiveMigration.
 */
@Database(entities = [Producto::class, User::class], version = 3, exportSchema = false)
abstract class GolgerBurguerDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao

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
                    // [CORREGIDO] Se especifica explícitamente que se deben borrar todas las tablas.
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                getDatabase(context).productDao().insertAll(FakeProductDataSource.products)
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
