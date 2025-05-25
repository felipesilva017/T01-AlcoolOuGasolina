package com.example.gasoralchool.Models.Fuel

import android.content.Context
import com.example.gasoralchool.Models.Repository

class FuelRepository(val context: Context) : Repository<Fuel> {
  override val key: String = "FuelRepository"

  override fun readAll(): List<Fuel> {
    return this.readHelper(context, Fuel::class.java)
  }

  override fun read(): Fuel {
    TODO("Not yet implemented")
  }

  override fun delete(id: String) {
    TODO("Not yet implemented")
  }

  override fun edit(id: String, objectModel: Fuel) {
    TODO("Not yet implemented")
  }

  override fun save(objectModel: Fuel) {
    this.saveHelper(context, objectModel, Fuel::class.java)
  }
}
