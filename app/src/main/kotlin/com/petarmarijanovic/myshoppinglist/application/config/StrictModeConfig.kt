package com.petarmarijanovic.myshoppinglist.application.config

import android.os.StrictMode
import com.petarmarijanovic.myshoppinglist.BuildConfig

/** Created by petar on 22/07/2017. */
class StrictModeConfig : ApplicationConfig {
  
  override fun configure() {
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
