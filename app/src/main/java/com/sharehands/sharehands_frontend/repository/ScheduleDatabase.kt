package com.sharehands.sharehands_frontend.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Equipment::class, MemoItem::class], version = 1)
abstract class ScheduleDatabase: RoomDatabase() {
    abstract fun equipmentDao(): EquipmentDao
    abstract fun memoItemDao(): MemoItemDao

    companion object {
        @Volatile
        private var INSTANCE: ScheduleDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ScheduleDatabase? {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, ScheduleDatabase::class.java, "schedule_mgt").build()
                INSTANCE = instance
                return instance
            }


        }
    }
}