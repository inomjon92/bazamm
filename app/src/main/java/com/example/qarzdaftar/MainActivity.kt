package com.example.qarzdaftar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.room.Room
import com.example.qarzdaftar.data.local.AppDatabase
import com.example.qarzdaftar.presentation.DebtViewModel
import com.example.qarzdaftar.presentation.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Ma'lumotlar bazasini ochish
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "qarz-database"
        ).build()

        val viewModel = DebtViewModel(database.debtDao())

        setContent {
            MaterialTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
