package com.petarmarijanovic.myshoppinglist.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Module
class AppModule {
  
  @Provides
  @Singleton
  fun context(application: Application): Context = application
}
