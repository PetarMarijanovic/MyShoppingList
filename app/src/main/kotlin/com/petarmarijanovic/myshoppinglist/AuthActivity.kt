package com.petarmarijanovic.myshoppinglist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.petarmarijanovic.myshoppinglist.screen.onboarding.OnBoardingActivity
import javax.inject.Inject

abstract class AuthActivity : AppCompatActivity() {
  
  @Inject
  lateinit var firebaseAuth: FirebaseAuth
  
  private val authStateListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener {
    if (it.currentUser == null) handleNoUser()
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MyShoppingListApplication).appComponent.inject(this)
    if (firebaseAuth.currentUser == null) handleNoUser()
  }
  
  override fun onStart() {
    super.onStart()
    firebaseAuth.addAuthStateListener(authStateListener)
  }
  
  override fun onStop() {
    firebaseAuth.removeAuthStateListener(authStateListener)
    super.onStop()
  }
  
  private fun handleNoUser() {
    finish()
    startActivity(Intent(this, OnBoardingActivity::class.java))
  }
}
