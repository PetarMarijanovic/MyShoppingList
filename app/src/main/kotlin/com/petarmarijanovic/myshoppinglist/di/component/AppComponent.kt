package com.petarmarijanovic.myshoppinglist.di.component

import com.petarmarijanovic.myshoppinglist.AuthActivity
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.di.module.FirebaseModule
import com.petarmarijanovic.myshoppinglist.screen.onboarding.OnBoardingActivity
import dagger.Component
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Singleton
@Component(modules = arrayOf(FirebaseModule::class))
interface AppComponent {
  
  fun inject(target: MyShoppingListApplication)
  
  fun inject(target: OnBoardingActivity)
  
  fun inject(target: AuthActivity)
  
  // TODO What if multiple modules for one component
  fun plusUserComponent(): UserComponent
  
}
