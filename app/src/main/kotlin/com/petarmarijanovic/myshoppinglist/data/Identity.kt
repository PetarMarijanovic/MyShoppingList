package com.petarmarijanovic.myshoppinglist.data

import com.google.firebase.database.DataSnapshot

/** Created by petar on 09/07/2017. */
data class Identity<T>(var id: String, var value: T) {
  
  companion object {
    fun <T> fromSnapshot(snapshot: DataSnapshot, clazz: Class<T>): Identity<T> {
      return Identity(snapshot.ref.key, snapshot.getValue(clazz)!!)
    }
  }
  
}
