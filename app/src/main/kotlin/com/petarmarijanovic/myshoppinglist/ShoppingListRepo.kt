package com.petarmarijanovic.myshoppinglist

import com.google.firebase.database.DatabaseReference
import com.petarmarijanovic.myshoppinglist.rxfirebase.Identity
import com.petarmarijanovic.myshoppinglist.rxfirebase.observe
import com.petarmarijanovic.myshoppinglist.rxfirebase.remove
import com.petarmarijanovic.myshoppinglist.rxfirebase.setValue
import io.reactivex.Observable

/** Created by petar on 09/07/2017. */
data class ShoppingList(val name: String)

class Repo<T>(private val ref: DatabaseReference, private val clazz: Class<T>) {
  
  fun observe(): Observable<List<Identity<T>>> =
      observe(ref).map { it.children }.map { it.map { Identity.fromSnapshot(it, clazz) }.toList() }
  
  fun observe(id: String): Observable<Identity<T>> =
      observe(ref.child(id)).map { Identity.fromSnapshot(it, clazz) }
  
  fun insert(item: T) = setValue(ref.push(), item)
  
  fun update(identity: Identity<T>) = setValue(ref.child(identity.id), identity.value)
  
  fun remove(id: String) = remove(ref.child(id))
}
