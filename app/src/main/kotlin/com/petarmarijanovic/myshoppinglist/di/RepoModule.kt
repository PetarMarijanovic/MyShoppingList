package com.petarmarijanovic.myshoppinglist.di

import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.Repo
import com.petarmarijanovic.myshoppinglist.ShoppingList
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Module
class RepoModule {
  
  companion object {
    private val SHOPPING_LIST = "shopping_list"
  }
  
  @Provides
  @Singleton
  internal fun shoppingListRepo(firebaseDatabase: FirebaseDatabase): Repo<ShoppingList> =
      Repo(firebaseDatabase.getReference(SHOPPING_LIST), ShoppingList::class.java)
  
}
