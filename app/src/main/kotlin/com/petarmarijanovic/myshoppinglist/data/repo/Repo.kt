package com.petarmarijanovic.myshoppinglist.data.repo

import com.google.firebase.database.DatabaseReference
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.rxfirebase.database.observe
import com.petarmarijanovic.myshoppinglist.rxfirebase.database.remove
import com.petarmarijanovic.myshoppinglist.rxfirebase.database.setValue
import io.reactivex.Observable

/** Created by petar on 09/07/2017. */
class Repo<T>(private val ref: DatabaseReference, private val clazz: Class<T>) {
  
  fun observe(): Observable<List<Identity<T>>> =
      observe(ref).map { it.children }.map { it.map { Identity.fromSnapshot(it, clazz) }.toList() }
  
  fun observe(id: String): Observable<Identity<T>> =
      observe(ref.child(id)).map { Identity.fromSnapshot(it, clazz) }
  
  fun insert(item: T) = setValue(ref.push(), item)
  
  fun update(identity: Identity<T>) = setValue(ref.child(identity.id), identity.value)
  
  fun remove(id: String) = remove(ref.child(id))
}
