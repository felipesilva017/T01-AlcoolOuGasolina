package com.example.gasoralchool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gasoralchool.view.AlcoolGasolinaPreco
import com.example.gasoralchool.view.InputView
import com.example.gasoralchool.view.Welcome
import com.example.gasoralchool.view.ui.theme.GasOrAlchoolTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      GasOrAlchoolTheme {
        val navController: NavHostController = rememberNavController()
        NavHost(navController = navController, startDestination = "welcome") {
          composable("welcome") { Welcome(navController) }
          // composable("input") { InputView(navController) }
          composable("ListaDePostos/$id") { InputView(navController) }
          composable("mainalcgas") { AlcoolGasolinaPreco(navController) }
        }
      }
    }
  }
}
