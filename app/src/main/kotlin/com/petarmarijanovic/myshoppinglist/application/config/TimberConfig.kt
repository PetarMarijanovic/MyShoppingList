package com.petarmarijanovic.myshoppinglist.application.config

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.petarmarijanovic.myshoppinglist.BuildConfig
import timber.log.Timber

/** Created by petar on 22/07/2017. */
class TimberConfig {
  
  fun configure() {
    if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    else Timber.plant(CrashlyticsTree())
  }
  
  internal class CrashlyticsTree : Timber.Tree() {
    override fun log(priority: Int, tag: String, message: String, throwable: Throwable?) {
      if (priority == Log.VERBOSE || priority == Log.DEBUG) return
      
      Crashlytics.log(priority, tag, message)
      throwable?.let { Crashlytics.logException(it) }
    }
  }
}
