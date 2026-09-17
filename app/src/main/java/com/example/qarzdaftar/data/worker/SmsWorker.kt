package com.example.qarzdaftar.data.worker

import android.content.Context
import android.telephony.SmsManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.room.Room
import com.example.qarzdaftar.data.local.AppDatabase
import kotlinx.coroutines.flow.first

class SmsWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val context = applicationContext
        val currentTime = System.currentTimeMillis()

        // Fonda ma'lumotlar bazasiga ulanish
        val database = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "qarz-database"
        ).build()
        
        val debtDao = database.debtDao()

        try {
            // Hali yopilmagan barcha faol qarzlarni olamiz
            val activeDebts = debtDao.getAllActiveDebts().first()

            for (debt in activeDebts) {
                // Agar qarz muddati kelgan bo'lsa va bu mijozga hali SMS ketmagan bo'lsa
                if (currentTime >= debt.dueDate && !debt.isSmsSent) {
                    val smsManager = context.getSystemService(SmsManager::class.java)
                    val smsMatni = "Salom ${debt.debtorName}! Qarz daftari eslatmasi: ${debt.description} uchun olingan ${debt.amount} so'mlik qarzni qaytarish muddati keldi."
                    
                    // SMS yuborish buyrug'i
                    smsManager.sendTextMessage(debt.phoneNumber, null, smsMatni, null, null)
                    
                    // Bazada ushbu mijozga SMS yuborilganligini belgilab qo'yamiz (qayta-qayta ketmasligi uchun)
                    debtDao.insertDebt(debt.copy(isSmsSent = true))
                }
            }
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry() // Xatolik bo'lsa qayta urinib ko'radi
        }
    }
}
