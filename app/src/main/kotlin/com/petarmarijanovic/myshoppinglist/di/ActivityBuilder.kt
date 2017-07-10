package com.petarmarijanovic.myshoppinglist.di

import com.petarmarijanovic.myshoppinglist.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/** Created by petar on 10/07/2017. */
@Module
abstract class ActivityBuilder {
  
  @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class))
  internal abstract fun mainActivity(): MainActivity
  
}
