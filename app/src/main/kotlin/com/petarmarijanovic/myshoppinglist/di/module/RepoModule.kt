package com.petarmarijanovic.myshoppinglist.di.module

import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingItemRepo
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingListRepo
import com.petarmarijanovic.myshoppinglist.di.component.UserComponent
import com.petarmarijanovic.myshoppinglist.di.scope.PerUser
import dagger.Module
import dagger.Provides
import javax.inject.Named

/** Created by petar on 10/07/2017. */
// TODO Check if repos are on MainThread
@Module
class RepoModule {
  
  @Provides
  @PerUser
  fun shoppingListRepo(@Named(UserComponent.NAMED_UID) uid: String,
                       firebaseDatabase: FirebaseDatabase) =
      ShoppingListRepo(uid, firebaseDatabase)
  
  @Provides
  @PerUser
  fun shoppingItemRepo(firebaseDatabase: FirebaseDatabase) = ShoppingItemRepo(firebaseDatabase)
  
}
