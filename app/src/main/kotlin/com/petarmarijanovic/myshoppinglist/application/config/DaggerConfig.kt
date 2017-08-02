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
  
  private fun createInitComponent(user: FirebaseUser) = application.createUserComponent(user.email!!)
  
  private fun updateComponent(user: Option<FirebaseUser>) =
      user.orNull()?.let { if (isDifferentEmail(it.email!!)) application.createUserComponent(it.email!!) }
          ?: application.clearUserComponent()
  
  private fun isDifferentEmail(email: String) = application.userComponent?.email() != email
}
