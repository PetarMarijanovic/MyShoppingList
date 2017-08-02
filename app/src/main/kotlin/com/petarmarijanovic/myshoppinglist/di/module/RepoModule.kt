package com.petarmarijanovic.myshoppinglist.di.module

import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.data.FirebaseReferences
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingItemRepo
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingListRepo
import com.petarmarijanovic.myshoppinglist.di.scope.PerUser
import dagger.Module
import dagger.Provides
import javax.inject.Named

/** Created by petar on 10/07/2017. */
// TODO Check if repos are on MainThread
@Module
class RepoModule {
  
  companion object {
    const val NAMED_EMAIL = "named_email"
  }
  
  @Provides
  @PerUser
  fun firebaseReferences(@Named(NAMED_EMAIL) email: String, database: FirebaseDatabase) =
      FirebaseReferences(email, database)
  
  @Provides
  @PerUser
  fun shoppingItemRepo(references: FirebaseReferences) = ShoppingItemRepo(references)
  
  @Provides
  @PerUser
  fun shoppingListRepo(@Named(NAMED_EMAIL) email: String,
                       references: FirebaseReferences,
                       itemRepo: ShoppingItemRepo) = ShoppingListRepo(email, references, itemRepo)
  
}
