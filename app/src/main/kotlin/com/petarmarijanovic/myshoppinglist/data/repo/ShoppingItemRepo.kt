package com.petarmarijanovic.myshoppinglist.data.repo

import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingItem
import com.petarmarijanovic.myshoppinglist.rxfirebase.database.observe
import com.petarmarijanovic.myshoppinglist.rxfirebase.database.remove
import com.petarmarijanovic.myshoppinglist.rxfirebase.database.setValue
import io.reactivex.Observable

/** Created by petar on 14/07/2017. */
class ShoppingItemRepo(val firebaseDatabase: FirebaseDatabase) {
  
  private val ref = firebaseDatabase.getReference("shopping_item")
  
  fun observe(listId: String): Observable<List<Identity<ShoppingItem>>> =
      observe(ref.child(listId))
          .map { it.children }
          .map { it.map { Identity.fromSnapshot(it, ShoppingItem::class.java) }.toList() }
  
  fun insert(item: ShoppingItem, listId: String) = setValue(ref.child(listId).push(), item)
  
  fun update(identity: Identity<ShoppingItem>, listId: String) =
      setValue(ref.child(listId).child(identity.id), identity.value)
  
  fun remove(id: String, listId: String) = remove(ref.child(listId).child(id))
  
}