package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.MetricType
import com.example.ui.HealthViewModel
import com.example.ui.screens.AddEntryScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private val viewModel: HealthViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        HealthApp(viewModel)
      }
    }
  }
}

@Composable
fun HealthApp(viewModel: HealthViewModel) {
  val navController = rememberNavController()

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      val navBackStackEntry by navController.currentBackStackEntryAsState()
      val currentDestination = navBackStackEntry?.destination
      
      val isAddScreen = currentDestination?.route?.startsWith("add_") == true

      if (!isAddScreen) {
        NavigationBar {
          NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = currentDestination?.hierarchy?.any { it.route == "dashboard" } == true,
            onClick = {
              navController.navigate("dashboard") {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
              }
            }
          )
          NavigationBarItem(
            icon = { Icon(Icons.Default.History, contentDescription = "History") },
            label = { Text("History") },
            selected = currentDestination?.hierarchy?.any { it.route == "history" } == true,
            onClick = {
              navController.navigate("history") {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
              }
            }
          )
        }
      }
    }
  ) { innerPadding ->
    NavHost(
      navController = navController,
      startDestination = "dashboard",
      modifier = Modifier.padding(innerPadding)
    ) {
      composable("dashboard") {
        DashboardScreen(
          viewModel = viewModel,
          onAddEntry = { type -> navController.navigate("add_${type.name}") }
        )
      }
      composable("history") {
        HistoryScreen(viewModel = viewModel)
      }
      composable(
        route = "add_{metricType}",
        arguments = listOf(navArgument("metricType") { type = NavType.StringType })
      ) { backStackEntry ->
        val typeString = backStackEntry.arguments?.getString("metricType") ?: MetricType.WATER.name
        val mType = try {
            MetricType.valueOf(typeString)
        } catch (e: Exception) {
            MetricType.WATER
        }
        
        AddEntryScreen(
          metricType = mType,
          onSave = { v1, v2, notes -> viewModel.addMetric(mType, v1, v2, notes) },
          onNavigateBack = { navController.popBackStack() }
        )
      }
    }
  }
}

