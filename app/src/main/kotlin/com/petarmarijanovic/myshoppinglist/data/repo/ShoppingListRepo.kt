package com.petarmarijanovic.myshoppinglist.data.repo

import com.androidhuman.rxfirebase2.database.dataChanges
import com.androidhuman.rxfirebase2.database.rxSetValue
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingList
import io.reactivex.Single

/** Created by petar on 20/07/2017. */
class ShoppingListRepo(val uid: String, val firebaseDatabase: FirebaseDatabase) {
  
  companion object {
    const val SHOPPING_LIST = "shopping_list"
  }
  
  fun insert(item: ShoppingList): Single<String> {
    val ref = firebaseDatabase.getReference(SHOPPING_LIST).child(uid).push()
    return ref.rxSetValue(item).toSingle { ref.key }
  }
  
  fun observe() =
      firebaseDatabase.getReference(SHOPPING_LIST).child(uid)
          .dataChanges()
          .map { it.children }
          .map { it.map { Identity(it.ref.key, it.getValue(ShoppingList::class.java)!!) }.toList() }
  
}
