package com.example.closetcompanion.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import android.content.Context
import androidx.room.TypeConverters

@Database(
    entities = [Closet::class, Outfit::class, Clothes::class, OutfitClothesJoin::class, ClosetOutfitJoin::class],
    version = 1
)

@TypeConverters(IntListConverter::class)

abstract class ClosetDatabase : RoomDatabase() {
    abstract fun closetDao(): ClosetDao
    abstract fun outfitDao(): OutfitDao
    abstract fun clothesDao(): ClothesDao

    companion object {
        @Volatile
        private var INSTANCE: ClosetDatabase? = null

        fun getDatabase(context: Context):  ClosetDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClosetDatabase::class.java,
                    "closet_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
