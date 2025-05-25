package com.example.gasoralchool.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gasoralchool.Models.Fuel.Fuel
import com.example.gasoralchool.Models.Fuel.FuelRepository
import com.example.gasoralchool.R
import com.example.gasoralchool.data.Coordenadas

@Composable
fun Welcome(
  navController: NavHostController
) { // Recebe o elemento que permite realizar a navegação

  val context = LocalContext.current
  val f = Fuel(name = "Teste 4", coordinates = Coordenadas(-23.5505, -46.6333))
  val r = FuelRepository(context)
  r.save(f)

  val fuel = r.readAll()

  Log.d("TESTE", "$fuel")

  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Text(text = "Bem-vindos ao Navigation Example!")
    Spacer(modifier = Modifier.height(16.dp))
    Image(
      painter = painterResource(id = R.drawable.welcome), // Substituir pelo seu recurso
      contentDescription = "Imagem de boas-vindas",
      modifier = Modifier.size(128.dp).clickable { navController.navigate("mainalcgas") },
    )
  }
}
