package com.petarmarijanovic.myshoppinglist.di.component

import com.petarmarijanovic.myshoppinglist.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.di.module.FirebaseModule
import com.petarmarijanovic.myshoppinglist.screen.lists.ListsActivity
import com.petarmarijanovic.myshoppinglist.screen.onboarding.OnBoardingActivity
import dagger.Component
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Singleton
@Component(modules = arrayOf(FirebaseModule::class))
interface AppComponent {
  
  fun inject(target: MyShoppingListApplication)
  
  // TODO For now
  fun inject(target: ListsActivity)
  
  fun inject(target: OnBoardingActivity)
  
  // TODO What if multiple modules for one component
  fun plus(): UserComponent
  
}
