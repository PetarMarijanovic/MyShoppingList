package com.petarmarijanovic.myshoppinglist.di.module

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.application.config.*
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import io.reactivex.Observable
import org.funktionale.option.Option
import javax.inject.Singleton

/** Created by petar on 10/07/2017. */
@Module
class ConfigModule {
  
  @Provides @IntoSet
  @Singleton
  fun daggerConfig(firebaseAuth: FirebaseAuth,
                   userObservable: Observable<Option<FirebaseUser>>,
                   application: MyShoppingListApplication): ApplicationConfig =
      DaggerConfig(firebaseAuth, userObservable, application)
  
  @Provides @IntoSet
  @Singleton
  fun leakCanaryConfig(application: MyShoppingListApplication): ApplicationConfig =
      LeakCanaryConfig(application)
  
  @Provides @IntoSet
  @Singleton
  fun timberConfig(): ApplicationConfig = TimberConfig()
  
  @Provides @IntoSet
  @Singleton
  fun crashlyticsConfig(context: Context,
                        userObservable: Observable<Option<FirebaseUser>>): ApplicationConfig =
      CrashlyticsConfig(context, userObservable)
  
  @Provides @IntoSet
  @Singleton
  fun firebaseConfig(firebaseDatabase: FirebaseDatabase): ApplicationConfig =
      FirebaseConfig(firebaseDatabase)
  
  @Provides @IntoSet
  @Singleton
  fun strictModeConfig(): ApplicationConfig = StrictModeConfig()
  
}
