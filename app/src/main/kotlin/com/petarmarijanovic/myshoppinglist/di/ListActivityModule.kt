package com.petarmarijanovic.myshoppinglist.di

import android.content.res.Resources
import com.petarmarijanovic.myshoppinglist.ListActivity
import dagger.Module
import dagger.Provides

/** Created by petar on 10/07/2017. */
@Module
class ListActivityModule {
  
  @Provides
  internal fun resources(mainActivity: ListActivity): Resources {
    return mainActivity.resources
  }
  
}
