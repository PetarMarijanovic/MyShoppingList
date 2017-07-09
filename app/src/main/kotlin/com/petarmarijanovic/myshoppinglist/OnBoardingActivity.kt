package com.petarmarijanovic.myshoppinglist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : AppCompatActivity() {
  
  private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
  private val authStateListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener {
    if (it.currentUser != null) {
      finish()
      startActivity(Intent(this, MainActivity::class.java))
    }
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_on_boarding)
    
    signup.setOnClickListener { signUp() }
    login.setOnClickListener { logIn() }
  }
  
  override fun onStart() {
    super.onStart()
    firebaseAuth.addAuthStateListener(authStateListener)
  }
  
  override fun onStop() {
    super.onStop()
    firebaseAuth.removeAuthStateListener(authStateListener)
  }
  
  private fun logIn() {
    firebaseAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
        .addOnCompleteListener(this) { task ->
          Log.d("Petarr", "signInWithEmail:onComplete:" + task.isSuccessful)
          
          // If sign in fails, display a message to the user. If sign in succeeds
          // the auth state listener will be notified and logic to handle the
          // signed in user can be handled in the listener.
          if (!task.isSuccessful) {
            Log.w("Petarr", "signInWithEmail:failed", task.exception)
            Toast.makeText(this@OnBoardingActivity, "Log in failed", Toast.LENGTH_SHORT).show()
          }
        }
  }
  
  private fun signUp() {
    firebaseAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
        .addOnCompleteListener(this) { task ->
          Log.d("Petarr", "createUserWithEmail:onComplete:" + task.isSuccessful)
          
          // If sign in fails, display a message to the user. If sign in succeeds
          // the auth state listener will be notified and logic to handle the
          // signed in user can be handled in the listener.
          if (!task.isSuccessful) {
            Log.w("Petarr", "signInWithEmail:failed", task.exception)
            Toast.makeText(this@OnBoardingActivity, "Sign up failed", Toast.LENGTH_SHORT).show()
          }
        }
  }
}
