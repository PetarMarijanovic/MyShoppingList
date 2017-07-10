package com.petarmarijanovic.myshoppinglist.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposables

/** Created by petar on 09/07/2017. */
class QueryChangesOnSubscribe(private val query: Query) : ObservableOnSubscribe<DataSnapshot> {
  
  override fun subscribe(emitter: ObservableEmitter<DataSnapshot>) {
    
    val listener = object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (!emitter.isDisposed) emitter.onNext(dataSnapshot)
      }
      
      override fun onCancelled(databaseError: DatabaseError) {
        if (!emitter.isDisposed) emitter.onError(databaseError.toException())
      }
    }
    
    query.addValueEventListener(listener)
    emitter.setDisposable(Disposables.fromAction({ query.removeEventListener(listener) }))
  }
}
