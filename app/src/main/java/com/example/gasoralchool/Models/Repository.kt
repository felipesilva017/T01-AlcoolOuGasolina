package com.example.gasoralchool.Models

import android.content.Context
import com.example.gasoralchool.R
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

interface Repository<Model> {
  val key: String

  fun readAll(): List<Model>

  fun read(): Model

  fun save(objectModel: Model)

  fun edit(id: String, objectModel: Model)

  fun delete(id: String)

  fun saveHelper(context: Context, model: Model, type: Class<Model>) {
    val allValues = readHelper(context, type).toMutableList()
    allValues.add(model)
    val json = Gson().toJson(allValues)
    val sharedPreferences =
      context.getSharedPreferences(
        context.getString(R.string.app_preferences_name),
        Context.MODE_PRIVATE,
      )
    val editableSharedPreferences = sharedPreferences.edit()
    editableSharedPreferences.putString(key, json).toString()
    editableSharedPreferences.apply()
    editableSharedPreferences.commit()
  }

  fun readHelper(context: Context, type: Class<Model>): List<Model> {
    val sharedPreferences =
      context.getSharedPreferences(
        context.getString(R.string.app_preferences_name),
        Context.MODE_PRIVATE,
      )
    val objectJson = sharedPreferences?.getString(key, "") ?: "[]"
    if (objectJson.isEmpty()) return emptyList()

    val typeToken = TypeToken.getParameterized(List::class.java, type).type
    return try {
      Gson().fromJson(objectJson, typeToken)
    } catch (err: JsonSyntaxException) {
      emptyList()
    }
  }
}
