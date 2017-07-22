package com.petarmarijanovic.myshoppinglist.application

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.BuildConfig
import com.petarmarijanovic.myshoppinglist.application.config.CrashlyticsConfig
import com.petarmarijanovic.myshoppinglist.application.config.FirebaseConfig
import com.petarmarijanovic.myshoppinglist.application.config.StrictModeConfig
import com.petarmarijanovic.myshoppinglist.application.config.TimberConfig
import com.petarmarijanovic.myshoppinglist.di.component.AppComponent
import com.petarmarijanovic.myshoppinglist.di.component.DaggerAppComponent
import com.petarmarijanovic.myshoppinglist.di.component.UserComponent
import com.squareup.leakcanary.LeakCanary
import io.reactivex.Observable
import org.funktionale.option.Option
import javax.inject.Inject

/** Created by petar on 10/07/2017. */
class MyShoppingListApplication : Application() {
  
  @Inject
  lateinit var firebaseDatabase: FirebaseDatabase
  
  @Inject
  lateinit var firebaseAuth: FirebaseAuth
  
  @Inject
  lateinit var userObservable: Observable<Option<FirebaseUser>>
  
  private var initUserId: String? = null
  lateinit var appComponent: AppComponent
  var userComponent: UserComponent? = null
  
  override fun onCreate() {
    super.onCreate()
    
    // LeakCanary
    if (LeakCanary.isInAnalyzerProcess(this)) return
    if (BuildConfig.DEBUG) LeakCanary.install(this)
    
    configureDagger()
    
    // Configs
    TimberConfig().configure()
    CrashlyticsConfig(this, userObservable).configure()
    FirebaseConfig(firebaseDatabase).configure()
    StrictModeConfig().configure()
  }
  
  private fun configureDagger() {
    appComponent = DaggerAppComponent.builder().build()
    appComponent.inject(this)
    
    firebaseAuth.currentUser?.let {
      initUserId = it.uid
      userComponent = appComponent.plusUserComponent()
    }
    
    userObservable.subscribe({ updateUserComponent(it) })
  }
  
  private fun updateUserComponent(it: Option<FirebaseUser>) {
    if (it.isDefined()) {
      if (it.get().uid != initUserId) userComponent = appComponent.plusUserComponent()
    } else userComponent = null
    
    initUserId = null
  }
}
