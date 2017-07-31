package com.petarmarijanovic.myshoppinglist.application.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.User
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
  
  private fun createInitComponent(firebaseUser: FirebaseUser) =
      application.createUserComponent(createUser(firebaseUser))
  
  private fun updateComponent(user: Option<FirebaseUser>) =
      user.orNull()?.let { if (isDifferentUid(it.uid)) application.createUserComponent(createUser(it)) }
          ?: application.clearUserComponent()
  
  // TODO Why do I need ? at user(), it shouldn't be able to be null
  private fun isDifferentUid(newUid: String) = application.userComponent?.user()?.id != newUid
  
  private fun createUser(firebaseUser: FirebaseUser) =
      Identity(firebaseUser.uid, User(firebaseUser.email!!))
}
