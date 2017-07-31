package com.petarmarijanovic.myshoppinglist.di.module

import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.User
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingListRepo
import com.petarmarijanovic.myshoppinglist.di.scope.PerUser
import dagger.Module
import dagger.Provides

/** Created by petar on 10/07/2017. */
// TODO Check if repos are on MainThread
@Module
class RepoModule {
  
  @Provides
  @PerUser
  fun shoppingListRepo(user: Identity<User>, firebaseDatabase: FirebaseDatabase) =
      ShoppingListRepo(user, firebaseDatabase)
  
}
