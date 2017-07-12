package com.petarmarijanovic.myshoppinglist.data.di

import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingList
import com.petarmarijanovic.myshoppinglist.data.repo.Repo
import com.petarmarijanovic.myshoppinglist.rxfirebase.di.FirebaseModule
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
// TODO Check if repos are on MainThread
@Module
class RepoModule {
  
  companion object {
    private val SHOPPING_LIST = "shopping_list"
  }
  
  @Provides
  @Singleton
  internal fun shoppingListRepo(@Named(FirebaseModule.NAMED_UID) uid: String?,
                                firebaseDatabase: FirebaseDatabase): Repo<ShoppingList> =
      Repo(firebaseDatabase.getReference(SHOPPING_LIST).child(uid), ShoppingList::class.java)
  
}
