package com.example.kylr.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kylr.data.backend.KylrVault
import com.example.kylr.data.model.Transaction
import com.example.kylr.data.model.TransactionStatus
import com.example.kylr.ui.navigation.Screen
import com.example.kylr.ui.theme.KYLRTheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val balance = KylrVault.getBalance("alexrivera@kylr")
    val history by KylrVault.transactionHistory.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceContainer
                        ) {
                            // Placeholder for profile image
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "KYLR",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Total Balance Card
            BalanceCard(balance)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Quick Actions
            QuickActionsGrid()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // People Section
            SectionHeader(title = "People", actionText = "View all")
            Spacer(modifier = Modifier.height(16.dp))
            PeopleRow(navController)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Recent Activity
            SectionHeader(title = "Recent Activity")
            Spacer(modifier = Modifier.height(16.dp))
            ActivityList(history.take(3)) // Show last 3
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Promotion Card
            PromotionCard()
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun BalanceCard(balance: Double) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "TOTAL BALANCE",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White.copy(alpha = 0.7f),
                    letterSpacing = 1.5.sp
                )
            )
            Text(
                text = "$${String.format(Locale.US, "%.2f", balance)}",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = MaterialTheme.colorScheme.primary),
                    shape = CircleShape
                ) {
                    Text("Add money", fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    border = ButtonDefaults.outlinedButtonBorder,
                    shape = CircleShape
                ) {
                    Text("Details", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun QuickActionsGrid() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        QuickActionItem(Icons.Outlined.QrCodeScanner, "Scan")
        QuickActionItem(Icons.AutoMirrored.Filled.Send, "Pay")
        QuickActionItem(Icons.Default.SwapHoriz, "Transfer")
        QuickActionItem(Icons.Default.Bolt, "Recharge")
    }
}

@Composable
fun QuickActionItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Icon(
                icon,
                contentDescription = label,
                modifier = Modifier.padding(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun SectionHeader(title: String, actionText: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        if (actionText != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun PeopleRow(navController: NavController) {
    val people = listOf(
        "Sarah" to "sarah@kylr",
        "Marcus" to "marcus@kylr",
        "Jamie" to "jamie@kylr",
        "Elena" to "elena@kylr",
    )
    LazyRow(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = Color.Transparent,
                    border = ButtonDefaults.outlinedButtonBorder
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.padding(16.dp), tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Add", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
        items(people) { (name, vpa) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { navController.navigate(Screen.SendMoney.createRoute(name, vpa)) }
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) { }
                Spacer(modifier = Modifier.height(8.dp))
                Text(name, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun ActivityList(transactions: List<Transaction>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (transactions.isEmpty()) {
                Text("No recent activity", modifier = Modifier.padding(16.dp), color = Color.Gray)
            } else {
                transactions.forEach { tx ->
                    ActivityItem(
                        icon = if (tx.status == TransactionStatus.SUCCESS) Icons.Default.CheckCircle else Icons.Default.Error,
                        title = tx.toVpa,
                        subtitle = if (tx.status == TransactionStatus.SUCCESS) "Success" else "Failed",
                        amount = "-$${String.format(Locale.US, "%.2f", tx.amount)}",
                        time = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date(tx.timestamp))
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityItem(icon: ImageVector, title: String, subtitle: String, amount: String, time: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.padding(12.dp), tint = MaterialTheme.colorScheme.secondary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(amount, fontWeight = FontWeight.Bold)
            Text(time, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@Composable
fun PromotionCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHighest
    ) {
        Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.tertiary
                ) {
                    Text(
                        "LIMITED OFFER",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 10.sp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Get up to $50 back on travel bookings", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { }) {
                    Text("Claim reward", fontWeight = FontWeight.Bold)
                }
            }
            // Placeholder for image
            Spacer(modifier = Modifier.width(16.dp))
            Surface(modifier = Modifier.size(100.dp), shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceDim) { }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Triple(Screen.Home, Icons.Default.Home, "Home"),
        Triple(Screen.Payments, Icons.Default.AccountBalanceWallet, "Payments"),
        Triple(Screen.History, Icons.Default.History, "History"),
        Triple(Screen.Profile, Icons.Default.Person, "Profile")
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
        tonalElevation = 0.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { (screen, icon, label) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                selected = currentRoute == screen.route,
                onClick = { 
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    KYLRTheme {
        HomeScreen(navController = rememberNavController())
    }
}
