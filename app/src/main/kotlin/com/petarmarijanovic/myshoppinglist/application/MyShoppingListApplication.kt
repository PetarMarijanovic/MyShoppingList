package com.petarmarijanovic.myshoppinglist.application

import android.app.Activity
import android.app.Application
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.BuildConfig
import com.petarmarijanovic.myshoppinglist.application.di.DaggerApplicationComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

/** Created by petar on 10/07/2017. */
class MyShoppingListApplication : Application(), HasActivityInjector {
  
  @Inject
  lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
  
  @Inject
  lateinit var firebaseDatabase: FirebaseDatabase
  
  override fun onCreate() {
    super.onCreate()
    
    DaggerApplicationComponent
        .builder()
        .build()
        .inject(this)
    
    firebaseDatabase.setPersistenceEnabled(true)
    
    if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    else Timber.plant(CrashReportingTree())
  }
  
  override fun activityInjector() = activityDispatchingAndroidInjector
}

// TODO Replace with crashlytics (or something else) for production
private class CrashReportingTree : Timber.Tree() {
  override fun log(priority: Int, tag: String, message: String, throwable: Throwable?) {
    if (priority == Log.VERBOSE || priority == Log.DEBUG) {
      return
    }
    
    //    FakeCrashLibrary.log(priority, tag, message)
    
    //    throwable?.let {
    //      if (priority == Log.WARN) FakeCrashLibrary.logWarning(throwable)
    //      else FakeCrashLibrary.logError(throwable)
    //    }
  }
}
