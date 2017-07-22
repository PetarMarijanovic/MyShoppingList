package com.petarmarijanovic.myshoppinglist.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Module
class FirebaseModule {
  
  @Provides
  @Singleton
  fun firebaseDatabase() = FirebaseDatabase.getInstance()
  
  @Provides
  @Singleton
  fun firebaseAuth() = FirebaseAuth.getInstance()
  
}
