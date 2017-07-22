package com.petarmarijanovic.myshoppinglist.di.module

import com.google.firebase.auth.FirebaseAuth
import com.petarmarijanovic.myshoppinglist.di.scope.PerUser
import dagger.Module
import dagger.Provides
import javax.inject.Named

/** Created by petar on 10/07/2017. */
@Module
class UserModule {
  
  companion object {
    const val NAMED_UID = "uid"
  }
  
  @Provides
  @PerUser
  @Named(NAMED_UID)
  fun uid(firebaseAuth: FirebaseAuth) = firebaseAuth.currentUser!!.uid
  
}
