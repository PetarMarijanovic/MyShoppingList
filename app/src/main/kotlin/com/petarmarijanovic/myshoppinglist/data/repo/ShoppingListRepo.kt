package com.petarmarijanovic.myshoppinglist.data.repo

import com.androidhuman.rxfirebase2.database.*
import com.google.android.gms.tasks.Task
import com.petarmarijanovic.myshoppinglist.data.DatabaseEvent
import com.petarmarijanovic.myshoppinglist.data.Event.ADD
import com.petarmarijanovic.myshoppinglist.data.Event.REMOVE
import com.petarmarijanovic.myshoppinglist.data.FirebaseReferences
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.decodeFromFirebaseKey
import com.petarmarijanovic.myshoppinglist.encodeAsFirebaseKey
import io.reactivex.Observable
import io.reactivex.functions.Function3
import java.util.*

/** Created by petar on 20/07/2017. */
data class ShoppingList(val name: String,
                        val users: List<String>,
                        val items: List<Identity<ShoppingItem>>)

class ShoppingListRepo(private val email: String,
                       references: FirebaseReferences,
                       private val itemsRepo: ShoppingItemRepo) {
  
  private val listsRef = references.lists
  private val listsPerUserRef = references.listsPerUser
  private val usersPerListRef = references.usersPerList
  
  fun add(name: String): String =
      listsRef.push().let {
        it.setValue(name)
        it.key.apply {
          // TODO Transaction or Firebase Rule
          usersPerListRef.child(this).child(email.encodeAsFirebaseKey()).setValue(true) // TODO Bezveze
          listsPerUserRef.child(this).setValue(true) // TODO Bezveze
        }
      }
  
  fun updateName(listId: String, name: String): Task<Void> = listsRef.child(listId).setValue(name)
  
  fun observeName(listId: String): Observable<String> =
      listsRef.child(listId).dataChanges().map { it.value as String }
  
  fun observeLists(): Observable<DatabaseEvent<Identity<ShoppingList>>> =
      userListIds().flatMap {
        val listId = it.item
        when (it.event) {
          ADD -> shoppingList(listId).map { DatabaseEvent(ADD, it) }
          REMOVE -> Observable.just(DatabaseEvent(REMOVE, Identity(listId, emptyList())))
          else -> throw IllegalArgumentException(it.toString() + " not supported")
        }
      }
  
  @Deprecated("This is here because in current implementation I need to add some object to DatabaseEvent for REMOVE",
              ReplaceWith("Nothing. Change implementation and then delete this method"))
  private fun emptyList() = ShoppingList("", Collections.emptyList(), Collections.emptyList())
  
  fun usersPerList(listId: String): Observable<List<String>> =
      usersPerListRef.child(listId).dataChanges().map { it.children.map { it.key.decodeFromFirebaseKey() } }
  
  fun usersPerListDataChanges(listId: String): Observable<DatabaseEvent<String>> =
      usersPerListRef.child(listId).rxChildEvents().map {
        val email = it.dataSnapshot().key.decodeFromFirebaseKey()
        when (it) {
          is ChildAddEvent -> DatabaseEvent(ADD, email)
          is ChildRemoveEvent -> DatabaseEvent(REMOVE, email)
          else -> throw IllegalArgumentException(it.toString() + " not supported")
        }
      }
  
  // TODO Write this without the ugly Function3
  private fun shoppingList(listId: String): Observable<Identity<ShoppingList>> =
      Observable.combineLatest(observeName(listId),
                               usersPerList(listId),
                               itemsRepo.dataChanges(listId),
                               Function3<String, List<String>, List<Identity<ShoppingItem>>, ShoppingList> { t1, t2, t3 ->
                                 ShoppingList(t1, t2, t3)
                               })
          .map { Identity(listId, it) }
  
  private fun userListIds() = listsPerUserRef.childEvents()
      .map {
        val listId = it.dataSnapshot().key
        when (it) {
          is ChildAddEvent -> DatabaseEvent<String>(ADD, listId)
          is ChildRemoveEvent -> DatabaseEvent<String>(REMOVE, listId)
          else -> throw IllegalArgumentException(it.toString() + " not supported")
        }
      }
}
