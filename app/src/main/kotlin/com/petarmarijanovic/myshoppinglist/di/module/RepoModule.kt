package com.petarmarijanovic.myshoppinglist.di.module

import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingItemRepo
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
  fun shoppingItemRepo(@Named(UserModule.NAMED_UID) uid: String) =
      ShoppingItemRepo(uid, FirebaseDatabase.getInstance())
  
}
