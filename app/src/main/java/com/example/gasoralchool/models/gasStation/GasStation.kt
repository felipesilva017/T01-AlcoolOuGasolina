package com.example.gasoralchool.models.gasStation

import java.io.Serializable
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom

data class GasStation(
  val id: String = UUID.randomUUID().toString(),
  val name: String?,
  val fuels: List<Fuel>,
  val coordinates: Coordinates = Coordinates(41.40338, 2.17403),
  val createdAt: String = getCurrentTime(),
) : Serializable {
  init {
    if (fuels.size < 2) throw Exception("Both gas and ethanol fuels are required")
  }

  fun whichFuelIsBetterOption(carHas75Efficiency: Boolean): Fuel {
    val proportionToCompare = if (carHas75Efficiency) 0.75 else 0.7
    val ethanol = fuels.find { it.name == "ethanol" } ?: throw Exception("Ethanol fuel not found")
    val gas = fuels.find { it.name == "gas" } ?: throw Exception("Gas fuel not found")
    val priceProportionBetweenFuels = ethanol.price / gas.price
    return if (priceProportionBetweenFuels > proportionToCompare) gas else ethanol
  }
}

fun getCurrentTime(): String {
  val utc = ZoneOffset.UTC
  val today = OffsetDateTime.now(utc).truncatedTo(ChronoUnit.SECONDS)
  val seventyYearsAgo = today.minusYears(70)
  val totalSeconds = ChronoUnit.SECONDS.between(seventyYearsAgo, today)
  val date = today.minusSeconds(ThreadLocalRandom.current().nextLong(0, totalSeconds))
  return date.toString()
}
