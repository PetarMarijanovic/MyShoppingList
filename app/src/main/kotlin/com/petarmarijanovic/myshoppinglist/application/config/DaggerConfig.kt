package com.petarmarijanovic.myshoppinglist.application.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import io.reactivex.Observable
import org.funktionale.option.Option

/** Created by petar on 22/07/2017. */
class DaggerConfig(val firebaseAuth: FirebaseAuth,
                   val userObservable: Observable<Option<FirebaseUser>>,
                   val application: MyShoppingListApplication) : ApplicationConfig {
  
  override fun configure() {
    firebaseAuth.currentUser?.let { createInitComponent(it) }
    userObservable.subscribe({ updateComponent(it) })
  }
  
  private fun createInitComponent(user: FirebaseUser) = application.createUserComponent(user.uid)
  
  private fun updateComponent(user: Option<FirebaseUser>) =
      user.orNull()?.let { if (isDifferentUid(it.uid)) application.createUserComponent(it.uid) }
          ?: application.clearUserComponent()
  
  private fun isDifferentUid(newUid: String) = application.userComponent?.uid() != newUid
}
