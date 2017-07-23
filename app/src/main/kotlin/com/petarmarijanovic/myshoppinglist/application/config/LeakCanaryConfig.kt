package com.petarmarijanovic.myshoppinglist.application.config

import com.petarmarijanovic.myshoppinglist.BuildConfig
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.squareup.leakcanary.LeakCanary

/** Created by petar on 23/07/2017. */
class LeakCanaryConfig(val application: MyShoppingListApplication) : ApplicationConfig {
  
  override fun configure() {
    if (BuildConfig.DEBUG) LeakCanary.install(application)
  }
}
