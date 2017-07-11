package com.petarmarijanovic.myshoppinglist.di

import android.app.Application
import com.petarmarijanovic.myshoppinglist.MyShoppingListApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class,
                             ActivityModules::class,
                             ApplicationModule::class,
                             FirebaseModule::class,
                             RepoModule::class))
interface ApplicationComponent {
  
  @Component.Builder
  interface Builder {
    @BindsInstance fun application(application: Application): Builder
    fun build(): ApplicationComponent
  }
  
  fun inject(app: MyShoppingListApplication)
}
