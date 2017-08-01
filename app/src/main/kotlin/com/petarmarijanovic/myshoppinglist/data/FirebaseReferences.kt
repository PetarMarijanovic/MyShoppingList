package com.petarmarijanovic.myshoppinglist.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.data.model.User

/** Created by petar on 01/08/2017. */
class FirebaseReferences(private val user: Identity<User>, private val database: FirebaseDatabase) {
  
  companion object {
    private const val LISTS = "lists"
    private const val USERS = "users"
    private const val INVITATIONS = "invitations"
    private const val ITEMS = "items"
  }
  
  fun items(listId: String): DatabaseReference =
      database.getReference(LISTS).child(listId).child(ITEMS)
  
}