package com.example.goldenburgers.model

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * [ACTUALIZADO] Se incrementa la versión de la base de datos a 3 por el cambio en la entidad User.
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

/**
 * [ACTUALIZADO] Añadida la función para actualizar un usuario.
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.Companion.ABORT)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    /**
     * [NUEVO] Actualiza un usuario existente en la base de datos.
     * Room usa la clave primaria (el 'id' del usuario) para encontrar la fila a actualizar.
     * @param user El objeto User con los datos actualizados.
     */
    @Update
    suspend fun updateUser(user: User)
}