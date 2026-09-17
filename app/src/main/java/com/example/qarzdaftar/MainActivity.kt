package com.example.qarzdaftar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.room.Room
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.qarzdaftar.data.local.AppDatabase
import com.example.qarzdaftar.data.worker.SmsWorker
import com.example.qarzdaftar.presentation.DebtViewModel
import com.example.qarzdaftar.presentation.ui.MainScreen
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Ma'lumotlar bazasini ochish
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "qarz-database"
        ).build()

        val viewModel = DebtViewModel(database.debtDao())

        // 2. Avtomat SMS tekshirish vazifasini har 24 soatda bir marta fonda ishga tushirish
        val smsCheckRequest = PeriodicWorkRequestBuilder<SmsWorker>(24, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "SmsAutoReminder",
            ExistingPeriodicWorkPolicy.KEEP, // Agar reja allaqachon qo'shilgan bo'lsa, uni o'zgartirmaydi
            smsCheckRequest
        )

        // 3. UI ekranni ko'rsatish (PIN-kod va asosiy sahifa)
        setContent {
            MaterialTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
