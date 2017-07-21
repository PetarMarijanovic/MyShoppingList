package com.petarmarijanovic.myshoppinglist.application.di

import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.data.di.RepoComponent
import com.petarmarijanovic.myshoppinglist.data.di.RepoModule
import com.petarmarijanovic.myshoppinglist.rxfirebase.di.FirebaseModule
import com.petarmarijanovic.myshoppinglist.screen.lists.ListsActivity
import com.petarmarijanovic.myshoppinglist.screen.onboarding.OnBoardingActivity
import dagger.Component
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Singleton
@Component(modules = arrayOf(FirebaseModule::class))
interface ApplicationComponent {
  
  fun inject(app: MyShoppingListApplication)
  
  fun inject(listsActivity: ListsActivity)
  
  fun inject(onBoardingActivity: OnBoardingActivity)
  
  fun plus(repoModule: RepoModule): RepoComponent
  
}
