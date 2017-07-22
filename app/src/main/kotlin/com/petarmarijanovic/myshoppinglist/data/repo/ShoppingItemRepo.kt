package com.petarmarijanovic.myshoppinglist.data.repo

import com.androidhuman.rxfirebase2.database.*
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.data.DatabaseEvent
import com.petarmarijanovic.myshoppinglist.data.Event
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingItem
import io.reactivex.Observable

/** Created by petar on 20/07/2017. */
// TODO Maybe listId in item
class ShoppingItemRepo(firebaseDatabase: FirebaseDatabase) {
  
  private val itemsRef = firebaseDatabase.getReference("shopping_items")
  
  fun insert(listId: String, item: ShoppingItem) = itemsRef.child(listId).push().rxSetValue(item)
  
  fun update(listId: String, id: String, field: String, value: Any) =
      itemsRef.child(listId).rxUpdateChildren(mapOf("/$id/$field" to value))
  
  fun delete(listId: String, id: String) = itemsRef.child(listId).child(id).rxRemoveValue()
  
  fun observe(listId: String): Observable<DatabaseEvent<Identity<ShoppingItem>>> =
      itemsRef.child(listId).rxChildEvents()
          .map({
                 val item = Identity.fromSnapshot(it.dataSnapshot(), ShoppingItem::class.java)
                 when (it) {
                   is ChildAddEvent -> DatabaseEvent(Event.ADD, item)
                   is ChildChangeEvent -> DatabaseEvent(Event.UPDATE, item)
                   is ChildMoveEvent -> throw IllegalArgumentException(it.toString() + " move not supported")
                   is ChildRemoveEvent -> DatabaseEvent(Event.REMOVE, item)
                   else -> throw IllegalArgumentException(it.toString() + " not supported")
                 }
               })
}
