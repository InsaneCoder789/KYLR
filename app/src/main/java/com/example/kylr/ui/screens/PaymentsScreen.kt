package com.example.kylr.ui.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kylr.data.payment.UpiQrParser
import com.example.kylr.ui.navigation.Screen
import com.example.kylr.ui.theme.KYLRTheme
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val activity = LocalContext.current as ComponentActivity
    
    val filteredContacts = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            allContacts
        } else {
            allContacts.filter { 
                it.name.contains(searchQuery, ignoreCase = true) || 
                it.upi.contains(searchQuery, ignoreCase = true) 
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = MaterialTheme.colorScheme.surfaceContainer) {}
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("KYLR", style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold))
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Outlined.Notifications, contentDescription = "Notifications") }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = {
                    val scanner = GmsBarcodeScanning.getClient(activity)
                    scanner.startScan()
                        .addOnSuccessListener { barcode ->
                            val raw = barcode.rawValue
                            if (raw.isNullOrBlank()) {
                                Toast.makeText(activity, "Empty QR payload", Toast.LENGTH_SHORT).show()
                                return@addOnSuccessListener
                            }
                            val parsed = UpiQrParser.parse(raw)
                            if (parsed == null) {
                                Toast.makeText(activity, "No UPI payment URI found in QR", Toast.LENGTH_LONG).show()
                                return@addOnSuccessListener
                            }
                            navController.navigate(
                                Screen.SendMoney.createRoute(parsed.displayName, parsed.payeeAddress),
                            )
                        }
                        .addOnFailureListener {
                            Toast.makeText(activity, "Scan failed: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(Icons.Outlined.QrCodeScanner, contentDescription = "Scan", modifier = Modifier.size(32.dp))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Send\nPayments", style = MaterialTheme.typography.headlineLarge, lineHeight = 36.sp)
                Text("To anyone on KYLR or via UPI ID", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(24.dp))
                
                // Search Bar
                PaymentSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
                Spacer(modifier = Modifier.height(32.dp))
                
                if (searchQuery.isEmpty()) {
                    SectionHeader(title = "Frequent", actionText = "VIEW ALL")
                    Spacer(modifier = Modifier.height(16.dp))
                    FrequentContacts(navController)
                    Spacer(modifier = Modifier.height(32.dp))
                }
                
                SectionHeader(title = if (searchQuery.isEmpty()) "All Contacts" else "Search Results")
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            items(filteredContacts) { contact ->
                ContactItem(contact) {
                    navController.navigate(Screen.SendMoney.createRoute(contact.name, contact.upi))
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            if (filteredContacts.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No contacts found", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun PaymentSearchBar(query: String, onQueryChange: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shadowElevation = 1.dp
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Name, Phone or UPI ID", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Color.Gray) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

@Composable
fun FrequentContacts(navController: NavController) {
    val people = listOf(
        "Sarah" to "sarah@kylr",
        "Marcus" to "marcus@kylr",
        "Elena" to "elena@kylr",
        "David" to "david@kylr",
        "Chloe" to "chloe@kylr",
    )
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(people) { (name, vpa) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { navController.navigate(Screen.SendMoney.createRoute(name, vpa)) }
            ) {
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    border = if (name == "Sarah") BorderStroke(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
                        )
                    ) else null
                ) { }
                Spacer(modifier = Modifier.height(8.dp))
                Text(name, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun ContactItem(contact: ContactData, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = if (contact.isInitials) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceContainerHighest
            ) {
                if (contact.isInitials) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(contact.initials, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(contact.name, fontWeight = FontWeight.Bold)
                Text(contact.upi, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

data class ContactData(val name: String, val upi: String, val isInitials: Boolean = false, val initials: String = "")

val allContacts = listOf(
    ContactData("Aarav Sharma", "aarav.sharma@okaxis"),
    ContactData("Ishani Patel", "ishani.patel@okicici"),
    ContactData("James Doe", "james.doe@okpaytm", true, "JD"),
    ContactData("Meera Reddy", "meera.reddy@okaxis"),
    ContactData("Rohan Gupta", "rohan.gupta@okicici")
)

@Preview(showBackground = true)
@Composable
fun PaymentsScreenPreview() {
    KYLRTheme {
        PaymentsScreen(navController = rememberNavController())
    }
}
