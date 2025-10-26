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
 * [CORREGIDO] Se usa la versión compatible de fallbackToDestructiveMigration.
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
                    // [CORREGIDO] Se usa la versión sin parámetros, compatible con la versión actual de Room.
                    .fallbackToDestructiveMigration()
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
