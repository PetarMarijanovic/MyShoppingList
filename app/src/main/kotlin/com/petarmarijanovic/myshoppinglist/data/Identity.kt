package com.petarmarijanovic.myshoppinglist.data

import com.google.firebase.database.DataSnapshot

/** Created by petar on 09/07/2017. */
data class Identity<T>(var id: String, var value: T) {
  
  companion object {
    fun <T> fromSnapshot(snapshot: DataSnapshot, clazz: Class<T>): Identity<T> {
      return Identity(decodeFromFirebaseKey(snapshot.ref.key), snapshot.getValue(clazz)!!)
    }
  }
  
}

fun encodeAsFirebaseKey(key: String) =
    key.replace("%", "%25")
        .replace(".", "%2E")
        .replace("#", "%23")
        .replace("$", "%24")
        .replace("/", "%2F")
        .replace("[", "%5B")
        .replace("]", "%5D")

fun decodeFromFirebaseKey(key: String) =
    key.replace("%25", "%")
        .replace("%2E", ".")
        .replace("%23", "#")
        .replace("%24", "$")
        .replace("%2F", "/")
        .replace("%5B", "[")
        .replace("%5D", "]")
