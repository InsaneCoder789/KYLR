package com.example.kylr.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("KYLR", style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold))
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Outlined.Tune, contentDescription = "Filter") }
                    IconButton(onClick = {}) { Icon(Icons.Outlined.Notifications, contentDescription = "Notifications") }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("History", style = MaterialTheme.typography.headlineLarge)
                Text("Tracking your flow across the ecosystem.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(24.dp))
                
                FilterChips()
                Spacer(modifier = Modifier.height(32.dp))
            }

            item { HistorySectionHeader("Today") }
            items(todayTransactions) { transaction ->
                TransactionItem(transaction)
                Spacer(modifier = Modifier.height(12.dp))
            }

            item { 
                Spacer(modifier = Modifier.height(24.dp))
                HistorySectionHeader("Yesterday") 
            }
            items(yesterdayTransactions) { transaction ->
                TransactionItem(transaction)
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun FilterChips() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(selected = true, label = "All Activities")
        FilterChip(selected = false, label = "Sent")
        FilterChip(selected = false, label = "Received")
    }
}

@Composable
fun FilterChip(selected: Boolean, label: String) {
    Surface(
        shape = CircleShape,
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelSmall.copy(color = if (selected) Color.White else MaterialTheme.colorScheme.onSecondaryContainer)
        )
    }
}

@Composable
fun HistorySectionHeader(title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp)) {
        Text(title.uppercase(), style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, letterSpacing = 1.sp))
        Spacer(modifier = Modifier.width(16.dp))
        HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.surfaceContainer)
    }
}

@Composable
fun TransactionItem(data: TransactionData) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = data.iconBg) {
                if (data.icon != null) {
                    Icon(data.icon, contentDescription = null, modifier = Modifier.padding(12.dp), tint = data.iconTint)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(data.name, fontWeight = FontWeight.Bold)
                Text(data.time, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(data.amount, fontWeight = FontWeight.ExtraBold, color = if (data.amount.startsWith("+")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                Text(data.category, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = Color.Gray)
            }
        }
    }
}

data class TransactionData(
    val name: String,
    val time: String,
    val amount: String,
    val category: String,
    val icon: ImageVector?,
    val iconBg: Color,
    val iconTint: Color = Color.Unspecified
)

val todayTransactions = listOf(
    TransactionData("Julian Alexander", "2:30 PM, Today", "-$12.00", "LUNCH SHARE", null, Color(0xFFE1E2ED)),
    TransactionData("Grid Solutions LLC", "10:15 AM, Today", "-$84.50", "UTILITIES", Icons.Default.ElectricBolt, Color(0xFFE8CDFD), Color(0xFF6B567F))
)

val yesterdayTransactions = listOf(
    TransactionData("Sarah Chen", "6:45 PM, Yesterday", "+$50.00", "FREELANCE PAY", null, Color(0xFFFAF8FE)),
    TransactionData("Urban Outfitters", "1:20 PM, Yesterday", "-$129.99", "SHOPPING", Icons.Default.ShoppingBag, Color(0xFFDCE2F9), Color(0xFF0B57D0))
)
