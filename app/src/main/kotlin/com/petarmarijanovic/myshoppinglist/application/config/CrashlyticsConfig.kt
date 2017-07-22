package com.petarmarijanovic.myshoppinglist.application.config

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.auth.FirebaseUser
import com.petarmarijanovic.myshoppinglist.BuildConfig
import io.fabric.sdk.android.Fabric
import io.reactivex.Observable
import org.funktionale.option.Option

/** Created by petar on 22/07/2017. */
class CrashlyticsConfig(private val context: Context,
                        private val userObservable: Observable<Option<FirebaseUser>>)
  : ApplicationConfig {
  
  override fun configure() {
    val crashlyticsKit = Crashlytics.Builder()
        .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
        .build()
    
    Fabric.with(Fabric.Builder(context).kits(crashlyticsKit).build())
    
    userObservable
        .filter { it.isDefined() }
        .map { it.get() }
        .subscribe({
                     Crashlytics.setUserIdentifier(it.uid)
                     Crashlytics.setUserEmail(it.email)
                     Crashlytics.setUserName(it.displayName)
                   })
  }
}