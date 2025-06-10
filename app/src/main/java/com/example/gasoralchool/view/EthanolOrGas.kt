package com.example.gasoralchool.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gasoralchool.R
import com.example.gasoralchool.models.gasStation.Fuel
import com.example.gasoralchool.models.gasStation.GasStation
import com.example.gasoralchool.models.gasStation.Coordinates
import com.example.gasoralchool.models.gasStation.GasStationRepository
import com.example.gasoralchool.models.userPreferences.UserPreferences
import com.example.gasoralchool.models.userPreferences.UserPreferencesRepository

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationServices

@Composable
fun EthanolOrGas(navController: NavHostController, id: String?) {
    val context = LocalContext.current
    val gasStationRepository = GasStationRepository(context)
    val gasStation = if (id != null) gasStationRepository.read(id) else null

    val userPreferences = UserPreferencesRepository(context)

    val initialName = gasStation?.name ?: ""
    val initialGas = gasStation?.fuels?.get(0)?.price?.toString() ?: ""
    val initialEthanol = gasStation?.fuels?.get(1)?.price?.toString() ?: ""

    var name by remember { mutableStateOf(initialName) }
    var ethanol by remember { mutableStateOf(initialEthanol) }
    var gas by remember { mutableStateOf(initialGas) }
    var checkedState by remember { mutableStateOf(userPreferences.read().carEfficiencyIs75) }
    var bestFuelResult by remember { mutableStateOf("") }

    fun saveGasStation(location: Location?) {

        val latitude = location?.let { it.latitude } ?: gasStation?.coordinates?.lat;
        val longitude = location?.let { it.longitude } ?: gasStation?.coordinates?.long;

        val newGasStation = GasStation(
            name = name,
            fuels = listOf(
                Fuel(name = "gas", price = gas.toDouble()),
                Fuel(name = "ethanol", price = ethanol.toDouble())
            ),
            coordinates =
                Coordinates(
                    lat = latitude,
                    long = longitude
                )
        )
        gasStationRepository.save(newGasStation)
    }

    fun editGasStation(location: Location?) {
        if (gasStation == null || id == null) return

        val latitude = location?.let { it.latitude } ?: gasStation.coordinates.lat;
        val longitude = location?.let { it.longitude } ?: gasStation.coordinates.long;

        val editedGasStation = gasStation.copy(
            name = name,
            fuels = listOf(
                Fuel(name = "gas", price = gas.toDouble()),
                Fuel(name = "ethanol", price = ethanol.toDouble()),
            ),
            coordinates =
                Coordinates(
                    lat = latitude,
                    long = longitude
                )
        )
        gasStationRepository.edit(id, editedGasStation)
    }


    @SuppressLint("MissingPermission")
    fun mutateGasStationAndNavigate() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (id == null) {
                    saveGasStation(location)
                } else {
                    editGasStation(location)
                }
                navController.popBackStack()
            }
            .addOnFailureListener {
                // Se falhar, salva mesmo sem coordenadas
                if (id == null) {
                    saveGasStation(null)
                } else {
                    editGasStation(null)
                }
                navController.popBackStack()
            }
    }

    fun calculateBestFuel(): String {
        return try {
            val tempGasStation = GasStation(
                name = name,
                fuels = listOf(
                    Fuel(name = "gas", price = gas.toDouble()),
                    Fuel(name = "ethanol", price = ethanol.toDouble())
                )
            )
            val bestFuel = tempGasStation.whichFuelIsBetterOption(checkedState)
            val resultString = if (bestFuel.name == "gas") {
                context.getString(R.string.gasoline_better)
            } else {
                context.getString(R.string.ethanol_better)
            }
            "${context.getString(R.string.result)}: $resultString"
        } catch (e: Exception) {
            "Erro no cálculo: ${e.message}"
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = ethanol,
                onValueChange = { ethanol = it },
                label = { Text(stringResource(R.string.ethanol_price_label)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            OutlinedTextField(
                value = gas,
                onValueChange = { gas = it },
                label = { Text(stringResource(R.string.gas_price_label)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.station_name_label)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(R.string.efficiency_percentage_label),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Switch(
                    modifier = Modifier.semantics { contentDescription = "Demo with icon" },
                    checked = checkedState,
                    onCheckedChange = {
                        checkedState = it
                        userPreferences.save(UserPreferences(checkedState))
                    },
                    thumbContent = {
                        if (checkedState) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    },
                )
            }

            Button(
                onClick = {
                    bestFuelResult = calculateBestFuel()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.calculate_text_submit_button)
                )
            }

            Button(
                onClick = {
                    mutateGasStationAndNavigate()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.insert_gas_station)
                )
            }

            Text(
                text = stringResource(R.string.calculate_prompt),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp),
            )

            Text(
                text = bestFuelResult,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}
