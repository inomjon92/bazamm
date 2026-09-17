package com.example.qarzdaftar.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.qarzdaftar.presentation.DebtViewModel

@Composable
fun MainScreen(viewModel: DebtViewModel) {
    val context = LocalContext.current
    
    // PIN-kod holatini tekshirish (1234 o'rniga o'zingiz xohlagan kodni yozishingiz mumkin)
    var isUnlocked by remember { mutableStateOf(false) }
    var pinCodeInput by remember { mutableStateOf("") }
    val correctPin = "1234" 

    // SMS yuborish ruxsatnomasi
    var hasSmsPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasSmsPermission = isGranted
            if (!isGranted) {
                Toast.makeText(context, "SMS ruxsati berilmadi! Avtomat eslatma ishlamaydi.", Toast.LENGTH_LONG).show()
            }
        }
    )

    // Ilova ochilganda SMS yuborishga ruxsat so'rash
    LaunchedEffect(key1 = true) {
        if (!hasSmsPermission) {
            launcher.launch(Manifest.permission.SEND_SMS)
        }
    }

    if (!isUnlocked) {
        // PIN-kod kiritish ekrani (Dizayni)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Qarz Daftariga kirish", style = MaterialTheme.typography.titleLarge)
                Text(text = "PIN-kodni kiriting", style = MaterialTheme.typography.bodyMedium)
                
                TextField(
                    value = pinCodeInput,
                    onValueChange = { 
                        if (it.length <= 4) pinCodeInput = it 
                        if (it == correctPin) isUnlocked = true
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = PasswordVisualTransformation(),
                    placeholder = { Text("****") },
                    singleLine = true,
                    modifier = Modifier.width(150.dp)
                )

                if (pinCodeInput.length == 4 && pinCodeInput != correctPin) {
                    Text(text = "Noto'g'ri kod! Qayta urinib ko'ring.", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    } else {
        // PIN-kod to'g'ri bo'lsa, asosiy qarzlar ro'yxati ochiladi
        DebtScreen(viewModel = viewModel)
    }
}
