package com.petarmarijanovic.myshoppinglist.di.module

import android.content.Context
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Module
class AppModule {
  
  @Provides
  @Singleton
  fun context(application: MyShoppingListApplication): Context = application
}
