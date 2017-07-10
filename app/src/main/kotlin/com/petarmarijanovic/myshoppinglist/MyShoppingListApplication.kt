package com.petarmarijanovic.myshoppinglist

import android.app.Activity
import android.app.Application
import com.petarmarijanovic.myshoppinglist.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/** Created by petar on 10/07/2017. */

class MyShoppingListApplication : Application(), HasActivityInjector {
  
  @Inject
  lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
  
  override fun onCreate() {
    super.onCreate()
    
    DaggerAppComponent
        .builder()
        .application(this)
        .build()
        .inject(this)
  }
  
  override fun activityInjector(): DispatchingAndroidInjector<Activity> {
    return activityDispatchingAndroidInjector
  }
}
