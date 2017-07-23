package com.petarmarijanovic.myshoppinglist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.screen.onboarding.OnBoardingActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.funktionale.option.Option
import javax.inject.Inject

abstract class AuthActivity : AppCompatActivity() {
  
  @Inject
  lateinit var firebaseAuth: FirebaseAuth
  
  @Inject
  lateinit var userObservable: Observable<Option<FirebaseUser>>
  
  private val subscription = CompositeDisposable()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MyShoppingListApplication).appComponent.inject(this)
    if (firebaseAuth.currentUser == null) startOnBoarding()
  }
  
  override fun onStart() {
    super.onStart()
    subscription.add(userObservable.filter { it.isEmpty() }.subscribe({ startOnBoarding() }))
  }
  
  override fun onStop() {
    subscription.clear()
    super.onStop()
  }
  
  private fun startOnBoarding() {
    finishAffinity()
    startActivity(Intent(this, OnBoardingActivity::class.java))
  }
}
