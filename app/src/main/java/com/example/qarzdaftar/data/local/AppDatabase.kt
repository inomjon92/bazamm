package com.example.qarzdaftar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.qarzdaftar.data.local.entity.DebtEntity

@Database(entities = [DebtEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun debtDao(): DebtDao
}
