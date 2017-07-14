package com.petarmarijanovic.myshoppinglist.application

import android.app.Activity
import android.app.Application
import android.os.StrictMode
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.BuildConfig
import com.petarmarijanovic.myshoppinglist.application.di.DaggerApplicationComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
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
    if (LeakCanary.isInAnalyzerProcess(this)) return
    
    leakCanary()
    crashlytics()
    dagger()
    firebase()
    timber()
    strictMode()
  }
  
  override fun activityInjector() = activityDispatchingAndroidInjector
  
  private fun leakCanary() {
    if (BuildConfig.DEBUG) LeakCanary.install(this)
  }
  
  private fun crashlytics() {
    val crashlyticsKit = Crashlytics.Builder()
        .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
        .build()
    
    Fabric.with(Fabric.Builder(this).kits(crashlyticsKit).build())
    
    // TODO: Move this to where you establish a user session
    // TODO: Use the current user's information
    //    Crashlytics.setUserIdentifier("12345")
    //    Crashlytics.setUserEmail("user@fabric.io")
    //    Crashlytics.setUserName("Test User")
  }
  
  private fun dagger() = DaggerApplicationComponent.builder().build().inject(this)
  
  private fun firebase() = firebaseDatabase.setPersistenceEnabled(true)
  
  private fun timber() {
    if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    else Timber.plant(CrashlyticsTree())
  }
  
  private fun strictMode() {
    if (BuildConfig.DEBUG) return
    
    StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                                   .detectAll()
                                   .penaltyLog()
                                   .penaltyDeath()
                                   .build())
    
    StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                               .detectAll()
                               .penaltyLog()
                               .penaltyDeath()
                               .build())
  }
}

private class CrashlyticsTree : Timber.Tree() {
  override fun log(priority: Int, tag: String, message: String, throwable: Throwable?) {
    if (priority == Log.VERBOSE || priority == Log.DEBUG) return
    
    Crashlytics.log(priority, tag, message)
    throwable?.let { Crashlytics.logException(it) }
  }
}
