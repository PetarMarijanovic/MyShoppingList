package com.petarmarijanovic.myshoppinglist.di

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Module
class FirebaseModule {
  
  @Provides
  @Singleton
  internal fun firebaseDatabase() = FirebaseDatabase.getInstance()
  
}
