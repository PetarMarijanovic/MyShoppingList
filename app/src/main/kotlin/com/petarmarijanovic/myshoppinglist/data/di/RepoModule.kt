package com.petarmarijanovic.myshoppinglist.data.di

import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.application.di.PerUser
import com.petarmarijanovic.myshoppinglist.data.ShoppingItemRepo
import dagger.Module
import dagger.Provides

/** Created by petar on 10/07/2017. */
// TODO Check if repos are on MainThread
// TODO https://github.com/PetarMarijanovic/MyShoppingList/issues/1
@Module
class RepoModule(val uid: String) {
  
  companion object {
    private val SHOPPING_LIST = "shopping_list"
  }
  
  @Provides
  @PerUser
  fun shoppingItemRepo() = ShoppingItemRepo(uid, FirebaseDatabase.getInstance())
  
}
