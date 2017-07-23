package com.petarmarijanovic.myshoppinglist.application.config

import android.app.Application
import com.petarmarijanovic.myshoppinglist.BuildConfig
import com.squareup.leakcanary.LeakCanary

/** Created by petar on 23/07/2017. */
class LeakCanaryConfig(val application: Application) : ApplicationConfig {
  
  override fun configure() {
    if (BuildConfig.DEBUG) LeakCanary.install(application)
  }
}
