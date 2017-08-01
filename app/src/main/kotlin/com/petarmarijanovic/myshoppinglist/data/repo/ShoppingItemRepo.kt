package com.petarmarijanovic.myshoppinglist.data.repo

import com.androidhuman.rxfirebase2.database.ChildAddEvent
import com.androidhuman.rxfirebase2.database.ChildChangeEvent
import com.androidhuman.rxfirebase2.database.ChildRemoveEvent
import com.androidhuman.rxfirebase2.database.rxChildEvents
import com.google.android.gms.tasks.Task
import com.petarmarijanovic.myshoppinglist.data.DatabaseEvent
import com.petarmarijanovic.myshoppinglist.data.Event
import com.petarmarijanovic.myshoppinglist.data.FirebaseReferences
import com.petarmarijanovic.myshoppinglist.data.Identity
import io.reactivex.Observable

/** Created by petar on 20/07/2017. */
// TODO Try with JsonProperties or what not
data class ShoppingItem(val checked: Boolean = false, val name: String = "", val quantity: Int = 0)

class ShoppingItemRepo(private val references: FirebaseReferences) {
  
  fun add(listId: String, item: ShoppingItem): Task<Void> =
      references.items(listId).push().setValue(item)
  
  fun updateChecked(listId: String, id: String, isChecked: Boolean) =
      update(listId, id, "checked", isChecked)
  
  fun updateName(listId: String, id: String, name: String) =
      update(listId, id, "name", name)
  
  fun updateQuantity(listId: String, id: String, quantity: Int) =
      update(listId, id, "quantity", quantity)
  
  fun remove(listId: String, id: String): Task<Void> =
      references.items(listId).child(id).removeValue()
  
  fun observe(listId: String): Observable<DatabaseEvent<Identity<ShoppingItem>>> =
      references.items(listId).rxChildEvents()
          .map({
                 val item = Identity.fromSnapshot(it.dataSnapshot(), ShoppingItem::class.java)
                 when (it) {
                   is ChildAddEvent -> DatabaseEvent(Event.ADD, item)
                   is ChildChangeEvent -> DatabaseEvent(Event.UPDATE, item)
                   is ChildRemoveEvent -> DatabaseEvent(Event.REMOVE, item)
                   else -> throw IllegalArgumentException("$it is not supported")
                 }
               })
  
  private fun update(listId: String, id: String, field: String, value: Any): Task<Void> =
      references.items(listId).updateChildren(mapOf("/$id/$field" to value))
}
