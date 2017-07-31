package com.petarmarijanovic.myshoppinglist.data.repo

import com.androidhuman.rxfirebase2.database.*
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.data.DatabaseEvent
import com.petarmarijanovic.myshoppinglist.data.Event
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingItem
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingList
import com.petarmarijanovic.myshoppinglist.data.model.User
import io.reactivex.Observable
import org.funktionale.option.Option
import org.funktionale.option.toOption

/** Created by petar on 20/07/2017. */
class ShoppingListRepo(private val user: Identity<User>, firebaseDatabase: FirebaseDatabase) {
  
  companion object {
    const val LISTS = "lists"
    const val USERS = "users"
    const val ITEMS = "items"
  }
  
  // TODO Share observables?
  
  private val listsRef = firebaseDatabase.getReference(LISTS)
  private val usersRef = firebaseDatabase.getReference(USERS).child(user.id).child("lists")
  
  fun newList(): String {
    val pushRef = listsRef.push()
    val listId = pushRef.key
    
    pushRef.child("users").child(user.id).setValue(user.value.copy(isAdmin = true))
    usersRef.child(listId).setValue(true) // TODO Bezveze
    
    return listId
  }
  
  fun updateName(listId: String, name: String): Task<Void> =
      listsRef.child(listId).child("name").setValue(name)
  
  fun name(listId: String): Observable<Option<String>> =
      listsRef.child(listId).child("name").dataChanges()
          .map { if (it.value != null) (it.value as String).toOption() else Option.None }
  
  fun list(id: String): Observable<DatabaseEvent<Identity<ShoppingList>>> =
      listsRef.childEvents().filter { it.dataSnapshot().key == id }
          .map {
            val item = toShoppingList(it.dataSnapshot())
            when (it) {
              is ChildAddEvent -> DatabaseEvent(Event.ADD, item)
              is ChildChangeEvent -> DatabaseEvent(Event.UPDATE, item)
              is ChildRemoveEvent -> DatabaseEvent(Event.REMOVE, item)
              else -> throw IllegalArgumentException(it.toString() + " not supported")
            }
          }
  
  fun lists(): Observable<DatabaseEvent<Identity<ShoppingList>>> =
      userListIds().flatMap {
        when (it.event) {
          Event.ADD -> list(it.item)
          Event.REMOVE -> Observable.just(
              DatabaseEvent(Event.REMOVE,
                            Identity(it.item, ShoppingList("For Now")))) // TODO
          else -> throw IllegalArgumentException(it.toString() + " not supported")
        }
      }
  
  private fun userListIds() = usersRef.childEvents()
      .map {
        val listId = it.dataSnapshot().key
        when (it) {
          is ChildAddEvent -> DatabaseEvent<String>(Event.ADD, listId)
          is ChildRemoveEvent -> DatabaseEvent<String>(Event.REMOVE, listId)
          else -> throw IllegalArgumentException(it.toString() + " not supported")
        }
      }
  
  fun deleteList(list: Identity<ShoppingList>) {
    // TODO Make this better with firebase functions
    // https://firebase.google.com/docs/functions/get-started
    usersRef.child(list.id).removeValue()
    if (list.value.users.size == 1) listsRef.child(list.id).removeValue()
    else listsRef.child(list.id).child("users").child(user.id).removeValue()
  }
  
  private fun toShoppingList(snapshot: DataSnapshot): Identity<ShoppingList> {
    val name = snapshot.child("name").value?.let { it as String } ?: ""
    
    val users = snapshot.child("users").children.map {
      Identity.fromSnapshot(it, User::class.java)
    }
    
    val items = snapshot.child("items").children.map {
      Identity.fromSnapshot(it, ShoppingItem::class.java)
    }
    
    return Identity(snapshot.key, ShoppingList(name, users, items))
  }
  
  fun addItem(listId: String, item: ShoppingItem): Task<Void> =
      listsRef.child(listId).child(ITEMS).push().setValue(item)
  
  fun updateItem(listId: String, id: String, field: String, value: Any) =
      listsRef.child(listId).child("items").updateChildren(mapOf("/$id/$field" to value))
  
  fun deleteItem(listId: String, id: String) =
      listsRef.child(listId).child("items").child(id).removeValue()
  
  fun deleteUser(listId: String, id: String) =
      listsRef.child(listId).child("users").child(id).removeValue()
  
  fun items(listId: String): Observable<DatabaseEvent<Identity<ShoppingItem>>> =
      listsRef.child(listId).child("items").rxChildEvents()
          .map({
                 val item = Identity.fromSnapshot(it.dataSnapshot(), ShoppingItem::class.java)
                 when (it) {
                   is ChildAddEvent -> DatabaseEvent(Event.ADD, item)
                   is ChildChangeEvent -> DatabaseEvent(Event.UPDATE, item)
                   is ChildRemoveEvent -> DatabaseEvent(Event.REMOVE, item)
                   else -> throw IllegalArgumentException(it.toString() + " not supported")
                 }
               })
  
  fun users(listId: String): Observable<DatabaseEvent<Identity<User>>> =
      listsRef.child(listId).child("users").rxChildEvents()
          .map({
                 val item = Identity.fromSnapshot(it.dataSnapshot(), User::class.java)
                 when (it) {
                   is ChildAddEvent -> DatabaseEvent(Event.ADD, item)
                   is ChildChangeEvent -> DatabaseEvent(Event.UPDATE, item)
                   is ChildRemoveEvent -> DatabaseEvent(Event.REMOVE, item)
                   else -> throw IllegalArgumentException(it.toString() + " not supported")
                 }
               })
}

