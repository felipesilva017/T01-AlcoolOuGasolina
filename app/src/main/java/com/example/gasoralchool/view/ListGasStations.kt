package com.example.gasoralchool.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gasoralchool.R
import com.example.gasoralchool.models.gasStation.GasStationRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListGasStations(navController: NavHostController, id: String?) {
  val context = LocalContext.current
  val gasStationRepository = GasStationRepository(context)
  val gasStationsList = gasStationRepository.readAll()
  Scaffold(
    topBar = { TopAppBar(title = { Text(context.getString(R.string.list_gas_station_title)) }) }
  ) { innerPadding ->
    Column {
      LazyColumn(
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        contentPadding = PaddingValues(16.dp),
      ) {
        items(gasStationsList) { item ->
          Card(
            onClick = {
              // Abrir Mapa
              // navController.navigate(Routes.GAS_STATION_FORM + "/$item.id")
              navController.navigate(Routes.GAS_STATION_FORM)
            },
            modifier = Modifier.fillMaxWidth().padding(10.dp),
          ) {
            Box(Modifier.fillMaxSize()) {
              Text(text = item.name.toString(), modifier = Modifier.padding(16.dp))
              Text(text = item.fuels.toString(), modifier = Modifier.padding(16.dp))
            }
          }
        }
      }
      Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.End,
      ) {
        FloatingActionButton(onClick = { navController.navigate(Routes.GAS_STATION_FORM) }) {
          Icon(Icons.Filled.Add, context.getString(R.string.insert_gas_station))
        }
      }
    }
  }
}
