package com.petarmarijanovic.myshoppinglist.application

import android.app.Application
import com.petarmarijanovic.myshoppinglist.application.config.ApplicationConfig
import com.petarmarijanovic.myshoppinglist.di.component.AppComponent
import com.petarmarijanovic.myshoppinglist.di.component.DaggerAppComponent
import com.petarmarijanovic.myshoppinglist.di.component.UserComponent
import com.squareup.leakcanary.LeakCanary
import javax.inject.Inject

/** Created by petar on 10/07/2017. */
class MyShoppingListApplication : Application() {
  
  // @JvmSuppressWildcards -> https://github.com/google/dagger/issues/668
  @Inject
  lateinit var applicationConfigs: Set<@JvmSuppressWildcards ApplicationConfig>
  
  private lateinit var appComponent: AppComponent
  private var userComponent: UserComponent? = null
  
  override fun onCreate() {
    super.onCreate()
    if (LeakCanary.isInAnalyzerProcess(this)) return
    
    appComponent = DaggerAppComponent.builder().application(this).build()
    appComponent.inject(this)
    
    applicationConfigs.forEach { it.configure() }
  }
  
  fun appComponent() = appComponent
  
  fun userComponent() = userComponent!!
  
  /** This method should be used only from DaggerConfig */
  fun createUserComponent(uid: String) {
    userComponent = appComponent.plusUserComponent().uid(uid).build()
  }
  
  /** This method should be used only from DaggerConfig */
  fun clearUserComponent() {
    userComponent = null
  }
}
