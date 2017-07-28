package com.petarmarijanovic.myshoppinglist.application.config

import android.os.StrictMode
import com.petarmarijanovic.myshoppinglist.BuildConfig

/** Created by petar on 22/07/2017. */
class StrictModeConfig : ApplicationConfig {
  
  override fun configure() {
    if (!BuildConfig.DEBUG) return
    
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

fun disableStrictMode(func: () -> Any?): Any? {
  if (!BuildConfig.DEBUG) return func()
  
  val oldThreadPolicy = StrictMode.getThreadPolicy()
  val oldVmPolicy = StrictMode.getVmPolicy()
  
  StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
  StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().penaltyLog().build())
  
  val anyValue = func()
  
  StrictMode.setThreadPolicy(oldThreadPolicy)
  StrictMode.setVmPolicy(oldVmPolicy)
  
  return anyValue
}
