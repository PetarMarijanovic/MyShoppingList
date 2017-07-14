package com.petarmarijanovic.myshoppinglist.application.di

import com.petarmarijanovic.myshoppinglist.screen.lists.ListsActivity
import com.petarmarijanovic.myshoppinglist.screen.onboarding.OnBoardingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/** Created by petar on 10/07/2017. */
@Module
abstract class ActivityInjector {
  
  @ContributesAndroidInjector
  internal abstract fun listsActivity(): ListsActivity
  
  @ContributesAndroidInjector
  internal abstract fun onBoardingActivity(): OnBoardingActivity
  
}
