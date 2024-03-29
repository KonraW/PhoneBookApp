package com.example.phonebookapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 3, exportSchema = false)
abstract class PeopleDatabase: RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: PeopleDatabase? = null

        fun getDatabase(context: Context): PeopleDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    PeopleDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}