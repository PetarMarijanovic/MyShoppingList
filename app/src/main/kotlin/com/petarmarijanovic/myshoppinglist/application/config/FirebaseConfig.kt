package com.petarmarijanovic.myshoppinglist.application.config

import com.google.firebase.database.FirebaseDatabase

/** Created by petar on 22/07/2017. */
class FirebaseConfig(val firebaseDatabase: FirebaseDatabase) {
  
  fun configure() = firebaseDatabase.setPersistenceEnabled(true)
}
