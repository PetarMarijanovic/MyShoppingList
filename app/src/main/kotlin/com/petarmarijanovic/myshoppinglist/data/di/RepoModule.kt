package com.petarmarijanovic.myshoppinglist.data.di

import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingList
import com.petarmarijanovic.myshoppinglist.rxfirebase.di.FirebaseModule
import dagger.Module
import dagger.Provides
import javax.inject.Named

/** Created by petar on 10/07/2017. */
// TODO Check if repos are on MainThread
// TODO https://github.com/PetarMarijanovic/MyShoppingList/issues/1
@Module
class RepoModule {
  
  companion object {
    private val SHOPPING_LIST = "shopping_list"
  }
  
}
