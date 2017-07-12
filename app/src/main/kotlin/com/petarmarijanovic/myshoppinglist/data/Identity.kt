package com.petarmarijanovic.myshoppinglist.data

import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson

/** Created by petar on 09/07/2017. */
data class Identity<T>(var id: String, var value: T) {
  
  companion object {
    val gson = Gson()
    
    fun <T> fromSnapshot(snapshot: DataSnapshot, clazz: Class<T>): Identity<T> {
      val json = gson.toJson(snapshot.value)
      return Identity(snapshot.ref.key, gson.fromJson(json, clazz))
    }
  }
}
