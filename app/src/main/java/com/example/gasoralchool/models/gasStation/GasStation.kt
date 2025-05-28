package com.example.gasoralchool.models.gasStation

import java.io.Serializable
import java.util.UUID

data class GasStation(
  val id: String = UUID.randomUUID().toString(),
  val name: String?,
  val fuels: List<Fuel>,
  val coordinates: Coordinates = Coordinates(41.40338, 2.17403),
) : Serializable {
  init {
    if (fuels.size < 2) throw Exception("Missing Gas or ethanol")
  }

  fun whichFuelIsBetterOption(carHas75Efficiency: Boolean): Fuel {
    val proportionToCompare = if (carHas75Efficiency) 0.75 else 0.7
    val ethanol = fuels.find { it.name == "ethanol" } ?: throw Exception("Ethanol fuel not found")
    val gas = fuels.find { it.name == "gas" } ?: throw Exception("Gas fuel not found")
    val priceProportionBetweenFuels = ethanol.price / gas.price
    return if (priceProportionBetweenFuels > proportionToCompare) gas else ethanol
  }
}
