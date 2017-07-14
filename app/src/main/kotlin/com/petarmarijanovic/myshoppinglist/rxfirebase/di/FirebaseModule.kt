package com.petarmarijanovic.myshoppinglist.rxfirebase.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Module
class FirebaseModule {
  
  companion object {
    const val NAMED_UID = "uid"
  }
  
  @Provides
  @Singleton
  internal fun firebaseDatabase() = FirebaseDatabase.getInstance()
  
  @Provides
  @Singleton
  internal fun firebaseAuth() = FirebaseAuth.getInstance()
  
  // TODO https://github.com/PetarMarijanovic/MyShoppingList/issues/1
  @Provides
  @Named(NAMED_UID)
  internal fun uid(firebaseAuth: FirebaseAuth) = firebaseAuth.currentUser?.uid ?: ""
  
}
