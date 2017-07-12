package com.petarmarijanovic.myshoppinglist.rxfirebase.database.observables

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import io.reactivex.CompletableEmitter
import io.reactivex.CompletableOnSubscribe

/** Created by petar on 09/07/2017. */
class RemoveValueOnSubscribe(private val ref: DatabaseReference) : CompletableOnSubscribe {
  
  override fun subscribe(emitter: CompletableEmitter) {
    
    val listener = OnCompleteListener<Void> { task ->
      if (!emitter.isDisposed) return@OnCompleteListener
      
      if (task.isSuccessful) emitter.onComplete()
      else task.exception?.let { emitter.onError(it) }
    }
    
    ref.removeValue().addOnCompleteListener(listener)
  }
}
