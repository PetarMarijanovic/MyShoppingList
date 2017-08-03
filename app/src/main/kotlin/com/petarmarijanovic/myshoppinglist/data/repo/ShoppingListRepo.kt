package com.petarmarijanovic.myshoppinglist.data.repo

import com.androidhuman.rxfirebase2.database.ChildAddEvent
import com.androidhuman.rxfirebase2.database.ChildRemoveEvent
import com.androidhuman.rxfirebase2.database.dataChanges
import com.androidhuman.rxfirebase2.database.rxChildEvents
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.data.DatabaseEvent
import com.petarmarijanovic.myshoppinglist.data.Event.*
import com.petarmarijanovic.myshoppinglist.data.FirebaseReferences
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.decodeFromFirebaseKey
import com.petarmarijanovic.myshoppinglist.encodeAsFirebaseKey
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import io.reactivex.subjects.PublishSubject
import java.util.*

/** Created by petar on 20/07/2017. */
data class ShoppingList(val name: String,
                        val users: List<String>,
                        val items: List<Identity<ShoppingItem>>)

class ShoppingListRepo(private val email: String,
                       private val references: FirebaseReferences,
                       private val itemsRepo: ShoppingItemRepo) {
  
  private val listsRef = references.lists
  private val listsPerUserRef = references.listsPerUser
  private val usersPerListRef = references.usersPerList
  
  fun observeLists(): Observable<DatabaseEvent<Identity<ShoppingList>>> {
    
    val subject = PublishSubject.create<DatabaseEvent<Identity<ShoppingList>>>()
    
    val listsPerUser = listsPerUser()
        .scan(HashMap<String, Disposable>(), { map, databaseEvent ->
          
          if (map.containsKey(databaseEvent.item)) {
            map.remove(databaseEvent.item)!!.dispose()
            subject.onNext(DatabaseEvent(REMOVE, Identity(databaseEvent.item, emptyList())))
            return@scan map
          }
          
          var isFirst = true
          
          val subscription = shoppingList(databaseEvent.item).map { DatabaseEvent(UPDATE, it) }
              .subscribe({
                           val event = if (isFirst) {
                             isFirst = false
                             ADD
                           } else it.event
                           subject.onNext(DatabaseEvent(event, it.item))
                         })
          
          map.put(databaseEvent.item, subscription)
          
          map
          
        }).subscribe()
    
    return subject.doOnDispose { listsPerUser.dispose() }
    
  }
  
  @Deprecated("This is here because in current implementation I need to add some object to DatabaseEvent for REMOVE",
              ReplaceWith("Nothing. Change implementation and then delete this method"))
  private fun emptyList() = ShoppingList("", Collections.emptyList(), Collections.emptyList())
  
  private fun listsPerUser() =
      listsPerUserRef.rxChildEvents().map {
        val listId = it.dataSnapshot().key
        when (it) {
          is ChildAddEvent -> DatabaseEvent(ADD, listId)
          is ChildRemoveEvent -> DatabaseEvent(REMOVE, listId)
          else -> throw IllegalArgumentException(it.toString() + " not supported")
        }
      }
  
  fun add(name: String): String {
    
    val newId = listsRef.push().key
    
    val encodedEmail = email.encodeAsFirebaseKey()
    val update = HashMap<String, Any>()
    
    update.put("lists/$newId/", name)
    update.put("usersPerList/$newId/$encodedEmail", true)
    update.put("listsPerUser/$encodedEmail/$newId", true)
    
    FirebaseDatabase.getInstance().reference.updateChildren(update)
    
    return newId
  }
  
  fun remove(listId: String, deleteList: Boolean) {
    val encodedEmail = email.encodeAsFirebaseKey()
    val update = HashMap<String, Any?>()
    
    if (deleteList) update.put("lists/$listId/", null)
    update.put("usersPerList/$listId/$encodedEmail/", null)
    update.put("listsPerUser/$encodedEmail/$listId", null)
    
    FirebaseDatabase.getInstance().reference.updateChildren(update)
  }
  
  fun updateName(listId: String, name: String): Task<Void> = listsRef.child(listId).setValue(name)
  
  fun observeName(listId: String): Observable<String> =
      listsRef.child(listId).dataChanges().filter { it.value != null }.map { it.value as String }
  
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
}
