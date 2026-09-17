package com.example.qarzdaftar.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.qarzdaftar.data.local.entity.DebtEntity
import com.example.qarzdaftar.presentation.DebtViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtScreen(viewModel: DebtViewModel) {
    val debts by viewModel.debtsState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Qarz Daftari") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(debts) { debt ->
                DebtItem(debt = debt, onSettleClick = { viewModel.settleDebt(debt.id) })
            }
        }

        if (showDialog) {
            AddDebtDialog(
                onDismiss = { showDialog = false },
                onSave = { name, phone, amount, desc, days ->
                    viewModel.addDebt(name, phone, amount, desc, days)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun DebtItem(debt: DebtEntity, onSettleClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = debt.debtorName, style = MaterialTheme.typography.titleMedium)
                Text(text = "Tel: ${debt.phoneNumber}", style = MaterialTheme.typography.bodySmall)
                Text(text = debt.description, style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${debt.amount} so'm",
                    color = if (debt.amount > 0) Color(0xFF4CAF50) else Color(0xFFF44336),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Button(onClick = onSettleClick, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)) {
                    Text("Yopish", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun AddDebtDialog(onDismiss: () -> Unit, onSave: (String, String, Double, String, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var days by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Yangi qarz qo'shish") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Ism") })
                TextField(value = phone, onValueChange = { phone = it }, label = { Text("Telefon raqam (+998...)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
                TextField(value = amount, onValueChange = { amount = it }, label = { Text("Summa (so'm)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                TextField(value = desc, onValueChange = { desc = it }, label = { Text("Izoh (Nima uchun)") })
                TextField(value = days, onValueChange = { days = it }, label = { Text("Necha kunga berildi? (Kun soni)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
        },
        confirmButton = {
            Button(onClick = { 
                onSave(name, phone, amount.toDoubleOrNull() ?: 0.0, desc, days.toIntOrNull() ?: 1) 
            }) {
                Text("Saqlash")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Bekor qilish") }
        }
    )
}
