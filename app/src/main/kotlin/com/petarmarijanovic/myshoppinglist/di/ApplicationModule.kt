package com.petarmarijanovic.myshoppinglist.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Module
class ApplicationModule {
  
  @Provides
  @Singleton
  internal fun context(application: Application): Context = application
  
}
