package com.example.qarzdaftar.data.local

import androidx.room.*
import com.example.qarzdaftar.data.local.entity.DebtEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtDao {
    // Faqat yopilmagan (faol) qarzlarni ro'yxatini olish
    @Query("SELECT * FROM debts WHERE isSettled = 0 ORDER BY timestamp DESC")
    fun getAllActiveDebts(): Flow<List<DebtEntity>>

    // Yangi qarz qo'shish yoki eskisini yangilash
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebt(debt: DebtEntity)

    // Qarzni yopilgan deb belgilash (isSettled = 1)
    @Query("UPDATE debts SET isSettled = 1 WHERE id = :debtId")
    suspend fun settleDebt(debtId: Int)
}
