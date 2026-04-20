package com.example.kylr.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kylr.BuildConfig
import com.example.kylr.data.payment.UpiIntentLauncher
import com.example.kylr.data.repository.PaymentBackendRepository
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMoneyScreen(navController: NavController, recipientName: String, payeeVpa: String) {
    var amount by remember { mutableStateOf("0") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isProcessing by remember { mutableStateOf(false) }
    val activity = LocalContext.current as ComponentActivity
    val backend = remember { PaymentBackendRepository() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Send Money", style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = MaterialTheme.colorScheme.surfaceContainerHighest) {}
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        color = Color.Transparent,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.tertiaryContainer,
                                        ),
                                    ),
                                ),
                        )
                    }
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp).align(Alignment.BottomEnd).background(Color.White, CircleShape),
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(recipientName, style = MaterialTheme.typography.headlineMedium)
                Text("UPI: $payeeVpa", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(40.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("ENTER AMOUNT (INR)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("₹", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(amount, style = MaterialTheme.typography.headlineLarge.copy(fontSize = 56.sp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Real settlement uses your bank UPI app (NPCI). KYLR server records are optional.",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader(title = "Pay from")
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 1.dp,
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(0.2f),
                    ) {
                        Icon(
                            Icons.Default.AccountBalance,
                            contentDescription = null,
                            modifier = Modifier.padding(12.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("UPI-linked account", fontWeight = FontWeight.Bold)
                        Text("Opened in PSP app when you pay", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.Default.ExpandMore, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            CustomKeypad(
                onKeyClick = { key ->
                    if (isProcessing) return@CustomKeypad
                    when (key) {
                        "back" -> if (amount.length > 1) amount = amount.dropLast(1) else amount = "0"
                        "." -> if (!amount.contains(".")) amount += "."
                        else -> {
                            if (amount == "0") amount = key
                            else amount += key
                        }
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            val amountVal = amount.toDoubleOrNull() ?: 0.0
            val amountForUpi = String.format(Locale.US, "%.2f", amountVal)

            Button(
                onClick = {
                    if (amountVal <= 0.0) return@Button
                    UpiIntentLauncher.launchPay(
                        context = activity,
                        payeeVpa = payeeVpa,
                        payeeName = recipientName,
                        amountInInr = amountForUpi,
                        transactionNote = "KYLR",
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                enabled = !isProcessing && amountVal > 0,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Pay with UPI app", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {
                    if (amountVal <= 0.0) return@OutlinedButton
                    if (BuildConfig.RAIL_API_BASE_URL.isBlank()) {
                        scope.launch { snackbarHostState.showSnackbar("Set rail.api.baseUrl in local.properties and run the Rail server.") }
                        return@OutlinedButton
                    }
                    isProcessing = true
                    scope.launch {
                        val req = backend.buildExecuteRequest(
                            payeeVpa = payeeVpa,
                            amountInInr = amountVal,
                            channel = "online",
                            offlineTokenId = null,
                        )
                        val result = backend.executeOnServer(req)
                        isProcessing = false
                        result.onSuccess {
                            snackbarHostState.showSnackbar("Pipeline recorded: ${it.result?.status}")
                            navController.popBackStack()
                        }.onFailure {
                            snackbarHostState.showSnackbar("Pipeline error: ${it.message}")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isProcessing && amountVal > 0,
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp))
                } else {
                    Text("Record on KYLR server (orchestration)")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Security model: secrets stay on the server; the app uses UPI intents for real money movement.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp,
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CustomKeypad(onKeyClick: (String) -> Unit) {
    val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "back")
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in 0 until 4) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                for (j in 0 until 3) {
                    val index = i * 3 + j
                    val key = keys[index]
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clickable { onKeyClick(key) },
                        color = Color.Transparent,
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (key == "back") {
                                Icon(Icons.AutoMirrored.Filled.Backspace, contentDescription = "Delete")
                            } else {
                                Text(key, style = MaterialTheme.typography.headlineSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
