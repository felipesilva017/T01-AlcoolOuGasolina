package com.example.gasoralchool.Models.Fuel

import com.example.gasoralchool.data.Coordenadas
import java.io.Serializable
import java.util.UUID

data class Fuel(
  val id: String = UUID.randomUUID().toString(),
  val name: String,
  val coordinates: Coordenadas = Coordenadas(41.40338, 2.17403),
) : Serializable
