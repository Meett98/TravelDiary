package com.example.mytodos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mytodos.entity.Entity

@Database(entities = [Entity::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDAO() : TodoDAO

    companion object {

        @Volatile
        private var INSTANCE: TodoDatabase? = null
        fun getDatabase(context: Context): TodoDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        TodoDatabase::class.java,
                        "todo_database"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}