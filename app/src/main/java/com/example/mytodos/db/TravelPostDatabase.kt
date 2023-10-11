package com.example.mytodos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mytodos.entity.EntityPost

@Database(entities = [EntityPost::class], version = 6)
abstract class TravelPostDatabase : RoomDatabase() {

    abstract fun travelpostDAO() : TravelPostDao

    companion object {

        @Volatile
        private var INSTANCE: TravelPostDatabase? = null





        val MIGRATION_5_6 = object : Migration(5, 6) {

//            override fun migrate(database: SupportSQLiteDatabase) {
//                // Create a new table with the added columns
//                database.execSQL("CREATE TABLE IF NOT EXISTS Travel_Diary_New (" +
//                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
//                        "postTitle TEXT NOT NULL, " +
//                        "postLocation TEXT NOT NULL, " +
//                        "username TEXT NOT NULL, " +
//                        "password TEXT NOT NULL, " +
//                        "imageResourceId INTEGER, " + // New column for drawable resource ID
//                        "imageUri TEXT)"               // New column for image URI
//                )
//
//                // Copy data from the old table to the new table
//                database.execSQL("INSERT INTO Travel_Diary_New (id, postTitle, postLocation, username, password, " +
//                        "imageResourceId, imageUri) " +
//                        "SELECT id, postTitle, postLocation, username, password, " +
//                        "imageResourceId, imageUri FROM Travel_Diary")
//
//                // Drop the old table
//                database.execSQL("DROP TABLE Travel_Diary")
//
//                // Rename the new table to the original table name
//                database.execSQL("ALTER TABLE Travel_Diary_New RENAME TO Travel_Diary")
//            }
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create a new table with the added columns
                database.execSQL("CREATE TABLE IF NOT EXISTS Travel_Diary_New (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "posttitle TEXT NOT NULL, " +
                        "location TEXT NOT NULL, " +
                        "username TEXT NOT NULL, " +
                        "password TEXT NOT NULL, " +
                        "imageUri TEXT)"               // New column for image URI
                )

                // Copy data from the old table to the new table
                database.execSQL("INSERT INTO Travel_Diary_New (id, posttitle, location, username, password, " +
                        "imageUri) " +
                        "SELECT id, posttitle, location, username, password, " +
                        "imageUri FROM Travel_Diary")

                // Drop the old table
                database.execSQL("DROP TABLE Travel_Diary")

                // Rename the new table to the original table name
                database.execSQL("ALTER TABLE Travel_Diary_New RENAME TO Travel_Diary")
            }
        }


        fun getDatabase(context: Context): TravelPostDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        TravelPostDatabase::class.java,
                        "travelpost_database"
                    ) .fallbackToDestructiveMigration() //.addMigrations(MIGRATION_5_6)
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}