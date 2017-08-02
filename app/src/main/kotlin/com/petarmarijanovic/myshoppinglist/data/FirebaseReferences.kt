package com.petarmarijanovic.myshoppinglist.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.encodeAsFirebaseKey

/** Created by petar on 01/08/2017. */
class FirebaseReferences(email: String, private val database: FirebaseDatabase) {
  
  companion object {
    private const val INVITATIONS = "invitations"
    private const val LISTS = "lists"
    private const val ITEMS_PER_LIST = "items_per_list"
    private const val LISTS_PER_USER = "lists_per_user"
    private const val USERS_PER_LIST = "users_per_list"
  }
  
  private val encodedEmail = email.encodeAsFirebaseKey()
  
  val lists: DatabaseReference = database.getReference(LISTS)
  val listsPerUser: DatabaseReference = database.getReference(LISTS_PER_USER).child(encodedEmail)
  val usersPerList: DatabaseReference = database.getReference(USERS_PER_LIST)
  
  fun items(listId: String): DatabaseReference = database.getReference(ITEMS_PER_LIST).child(listId)
  
}
