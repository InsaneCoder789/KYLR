package com.example.kylr.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kylr.ui.screens.*

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object BankSelection : Screen("bank_selection")
    object Home : Screen("home")
    object Payments : Screen("payments")
    object History : Screen("history")
    object Profile : Screen("profile")
    object SendMoney : Screen("send_money/{recipientName}/{payeeVpa}") {
        fun createRoute(recipientName: String, payeeVpa: String): String {
            val rn = android.net.Uri.encode(recipientName)
            val pv = android.net.Uri.encode(payeeVpa)
            return "send_money/$rn/$pv"
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController, startDestination: String = Screen.Onboarding.route) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController)
        }
        composable(Screen.BankSelection.route) {
            BankSelectionScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Payments.route) {
            PaymentsScreen(navController)
        }
        composable(Screen.History.route) {
            HistoryScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Screen.SendMoney.route) { backStackEntry ->
            val recipientNameArg = backStackEntry.arguments?.getString("recipientName").orEmpty()
            val payeeVpaArg = backStackEntry.arguments?.getString("payeeVpa").orEmpty()
            val recipientName = android.net.Uri.decode(recipientNameArg)
            val payeeVpa = android.net.Uri.decode(payeeVpaArg)
            SendMoneyScreen(navController, recipientName, payeeVpa)
        }
    }
}
