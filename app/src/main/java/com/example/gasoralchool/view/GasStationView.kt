package com.example.gasoralchool.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gasoralchool.R
import com.example.gasoralchool.models.gasStation.GasStation
import com.example.gasoralchool.models.gasStation.GasStationRepository
import com.example.gasoralchool.util.openInMap
import com.example.gasoralchool.view.Routes
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun formatDate(isoDate: String): String {
  return try {
    val zonedDateTime = ZonedDateTime.parse(isoDate)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    zonedDateTime.format(formatter)
  } catch (e: DateTimeParseException) {
    isoDate
  }
}

fun calculateComparisonResult(
  context: android.content.Context,
  ethanolPrice: Double,
  gasPrice: Double
): String {
  val resultString = if (ethanolPrice / gasPrice < 0.7) {
    context.getString(R.string.ethanol_better)
  } else {
    context.getString(R.string.gasoline_better)
  }

  return "${context.getString(R.string.result)}: $resultString"
}


@Composable
fun GasStationView(navController: NavHostController, id: String?) {
  val context = LocalContext.current
  val gasStationRepository = remember { GasStationRepository(context) }

  val gasStationState = remember { mutableStateOf<GasStation?>(null) }
  val errorState = remember { mutableStateOf(false) }

  LaunchedEffect(id) {
    if (id == null) {
      errorState.value = true
      return@LaunchedEffect
    }

    try {
      gasStationState.value = gasStationRepository.read(id)
    } catch (e: Exception) {
      errorState.value = true
    }

    if (errorState.value || gasStationState.value == null) {
      Toast.makeText(context, context.getString(R.string.gas_station_not_found), Toast.LENGTH_SHORT).show()
      navController.navigate(Routes.GAS_STATION)
    }
  }

  val gasStation = gasStationState.value ?: return

  val name = gasStation.name.orEmpty()
  val createdAtFormatted = gasStation.createdAt?.let { formatDate(it) }.orEmpty()
  val gasPrice = gasStation.fuels.getOrNull(0)?.price ?: 0.0
  val ethanolPrice = gasStation.fuels.getOrNull(1)?.price ?: 0.0

  val gas = gasPrice.toString()
  val ethanol = ethanolPrice.toString()

  val comparisonResult = calculateComparisonResult(context, ethanolPrice, gasPrice)

  fun editGasStation() {
    navController.navigate("${Routes.GAS_STATION_FORM}/$id")
  }

  fun deleteGasStation() {
    id?.let {
      gasStationRepository.delete(it)
      navController.navigate(Routes.GAS_STATION)
    }
  }

  Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    Column(
      modifier = Modifier
        .wrapContentSize(Alignment.Center)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(name)
      Text(gas)
      Text(ethanol)
      Text(createdAtFormatted)
      Text(comparisonResult)

      Button(onClick = { editGasStation() }) {
        Text(stringResource(R.string.edit_gas_station_button))
      }
      Button(onClick = { deleteGasStation() }) {
        Text(stringResource(R.string.delete_gas_station_button))
      }
      Button(onClick = {
        Log.v("GasStationView", "Lat: ${gasStation.coordinates.lat}, Long: ${gasStation.coordinates.long}")
        openInMap(context, gasStation.coordinates.lat, gasStation.coordinates.long)
      }) {
        Text(stringResource(R.string.view_on_map))
      }
    }
  }
}
