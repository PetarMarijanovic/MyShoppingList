package com.petarmarijanovic.myshoppinglist.di.module

import com.androidhuman.rxfirebase2.auth.authStateChanges
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import org.funktionale.option.toOption
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Module
class FirebaseModule {
  
  @Provides
  @Singleton
  fun firebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()
  
  @Provides
  @Singleton
  fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
  
  @Provides
  @Singleton
  fun userObservable(firebaseAuth: FirebaseAuth) = firebaseAuth.authStateChanges()
      .map { it.currentUser.toOption() }
      .distinctUntilChanged()
      .replay(1).refCount()
}
