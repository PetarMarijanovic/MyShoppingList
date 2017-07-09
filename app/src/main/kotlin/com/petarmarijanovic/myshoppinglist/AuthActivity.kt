package com.petarmarijanovic.myshoppinglist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

abstract class AuthActivity : AppCompatActivity() {
  
  private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
  private val authStateListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener {
    if (it.currentUser == null) {
      finish()
      startActivity(Intent(this, OnBoardingActivity::class.java))
    }
  }
  
  override fun onStart() {
    super.onStart()
    firebaseAuth.addAuthStateListener(authStateListener)
  }
  
  override fun onStop() {
    super.onStop()
    firebaseAuth.removeAuthStateListener(authStateListener)
  }
}
