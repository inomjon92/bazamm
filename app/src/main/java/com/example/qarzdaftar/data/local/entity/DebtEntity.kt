package com.example.qarzdaftar.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debts")
data class DebtEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val debtorName: String,         // Mijozning ismi
    val phoneNumber: String,        // Telefon raqami (+998...)
    val amount: Double,             // Qarz summasi
    val description: String,        // Izoh (nima uchun olingani)
    val timestamp: Long,            // Qarz yozilgan vaqt
    val dueDate: Long,              // Qarz qaytarilishi kerak bo'lgan kun (sana)
    val isSettled: Boolean = false, // Qarz uzildimi? (true/false)
    val isSmsSent: Boolean = false  // SMS eslatma ketdimi?
)
