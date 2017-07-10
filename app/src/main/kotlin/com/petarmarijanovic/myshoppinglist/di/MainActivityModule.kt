package com.petarmarijanovic.myshoppinglist.di

import android.content.res.Resources
import com.petarmarijanovic.myshoppinglist.MainActivity
import dagger.Module
import dagger.Provides

/** Created by petar on 10/07/2017. */
@Module
class MainActivityModule {
  
  @Provides
  internal fun resources(mainActivity: MainActivity): Resources? {
    return mainActivity.resources
  }
  
}
