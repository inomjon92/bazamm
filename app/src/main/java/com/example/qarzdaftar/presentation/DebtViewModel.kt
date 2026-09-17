package com.example.qarzdaftar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qarzdaftar.data.local.DebtDao
import com.example.qarzdaftar.data.local.entity.DebtEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DebtViewModel(private val debtDao: DebtDao) : ViewModel() {

    // Ekran doimiy kuzatib boradigan faol qarzlar ro'yxati
    val debtsState = debtDao.getAllActiveDebts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Yangi qarz qo'shish funksiyasi
    fun addDebt(name: String, phone: String, amount: Double, desc: String, daysToReturn: Int) {
        viewModelScope.launch {
            if (name.isNotBlank() && phone.isNotBlank() && amount != 0.0) {
                val currentTime = System.currentTimeMillis()
                // Kunlarni millisekundlarga hisoblash (1 kun = 24 * 60 * 60 * 1000)
                val dueDateInMillis = currentTime + (daysToReturn.toLong() * 24 * 60 * 60 * 1000)

                val newDebt = DebtEntity(
                    debtorName = name,
                    phoneNumber = phone,
                    amount = amount,
                    description = desc,
                    timestamp = currentTime,
                    dueDate = dueDateInMillis
                )
                debtDao.insertDebt(newDebt)
            }
        }
    }

    // Qarz uzilganda uni yopish (arxivlash) funksiyasi
    fun settleDebt(id: Int) {
        viewModelScope.launch {
            debtDao.settleDebt(id)
        }
    }
}
