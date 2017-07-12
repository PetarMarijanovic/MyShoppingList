package com.petarmarijanovic.myshoppinglist.application.di

import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.data.di.RepoModule
import com.petarmarijanovic.myshoppinglist.rxfirebase.di.FirebaseModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class,
                             ActivityInjector::class,
                             FirebaseModule::class,
                             RepoModule::class))
interface ApplicationComponent {
  
  @Component.Builder
  interface Builder {
    fun build(): ApplicationComponent
  }
  
  fun inject(app: MyShoppingListApplication)
}
