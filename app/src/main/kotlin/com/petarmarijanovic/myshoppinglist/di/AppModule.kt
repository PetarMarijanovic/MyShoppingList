package com.petarmarijanovic.myshoppinglist.di

import android.app.Application
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.Repo
import com.petarmarijanovic.myshoppinglist.ShoppingList
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Module
class AppModule {
  
  @Provides
  @Singleton
  internal fun context(application: Application): Context = application
  
  @Provides
  @Singleton
  internal fun firebaseDatabase() = FirebaseDatabase.getInstance()
  
  @Provides
  @Singleton
  internal fun shoppingListRepo(firebaseDatabase: FirebaseDatabase): Repo<ShoppingList> =
      Repo(firebaseDatabase.getReference("shopping_list"), ShoppingList::class.java)
  
}
