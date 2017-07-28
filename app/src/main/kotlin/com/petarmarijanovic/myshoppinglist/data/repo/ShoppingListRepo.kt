package com.petarmarijanovic.myshoppinglist.data.repo

import com.androidhuman.rxfirebase2.database.dataChanges
import com.androidhuman.rxfirebase2.database.rxSetValue
import com.androidhuman.rxfirebase2.database.rxUpdateChildren
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingList

/** Created by petar on 20/07/2017. */
class ShoppingListRepo(val uid: String, val firebaseDatabase: FirebaseDatabase) {
  
  companion object {
    const val SHOPPING_LIST = "shopping_list"
  }
  
  private val ref = firebaseDatabase.getReference(SHOPPING_LIST).child(uid)
  
  fun insert(item: ShoppingList) = ref.push().rxSetValue(item).toSingle { ref.key }
  
  fun update(id: String, field: String, value: Any) =
      ref.rxUpdateChildren(mapOf("/$id/$field" to value))
  
  fun observe(id: String) =
      ref.child(id)
          .dataChanges()
          .map { Identity(it.ref.key, it.getValue(ShoppingList::class.java)!!) }
  
  fun observe() =
      ref.dataChanges()
          .map { it.children }
          .map { it.map { Identity(it.ref.key, it.getValue(ShoppingList::class.java)!!) }.toList() }
  
}
