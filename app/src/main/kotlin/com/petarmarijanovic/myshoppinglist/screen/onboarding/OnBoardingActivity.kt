package com.petarmarijanovic.myshoppinglist.screen.onboarding

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.*
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.screen.lists.ListsActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.screen_on_boarding.*
import org.funktionale.option.Option
import java.lang.Exception
import javax.inject.Inject

class OnBoardingActivity : AppCompatActivity() {
  
  @Inject
  lateinit var firebaseAuth: FirebaseAuth
  
  @Inject
  lateinit var userObservable: Observable<Option<FirebaseUser>>
  
  private val subscription = CompositeDisposable()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MyShoppingListApplication).appComponent.inject(this)
    setContentView(R.layout.screen_on_boarding)
    
    signup.setOnClickListener { signUp() }
    login.setOnClickListener { logIn() }
  }
  
  override fun onStart() {
    super.onStart()
    subscription.add(userObservable.filter { it.nonEmpty() }.subscribe({ finishOnBoarding() }))
  }
  
  override fun onStop() {
    subscription.clear()
    super.onStop()
  }
  
  private fun finishOnBoarding() {
    finishAffinity()
    startActivity(Intent(this, ListsActivity::class.java))
  }
  
  private fun logIn() {
    firebaseAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
        .addOnCompleteListener(this) { task ->
          if (!task.isSuccessful) {
            val errorMessage = logInErrorMessage(task.exception)
            Toast.makeText(this@OnBoardingActivity, errorMessage, Toast.LENGTH_SHORT).show()
          }
        }
  }
  
  private fun signUp() {
    firebaseAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
        .addOnCompleteListener(this) { task ->
          if (!task.isSuccessful) {
            val errorMessage = signUpErrorMessage(task.exception)
            Toast.makeText(this@OnBoardingActivity, errorMessage, Toast.LENGTH_SHORT).show()
          }
        }
  }
  
  // https://developers.google.com/android/reference/com/google/firebase/auth/FirebaseAuth#exceptions
  private fun signUpErrorMessage(exception: Exception?) =
      when (exception) {
        is FirebaseAuthWeakPasswordException -> exception.reason
        is FirebaseAuthInvalidCredentialsException -> getString(R.string.error_malformed_email)
        is FirebaseAuthUserCollisionException -> getString(R.string.error_email_already_exists)
        else -> getString(R.string.general_sign_up_log_in_error)
      }
  
  // https://developers.google.com/android/reference/com/google/firebase/auth/FirebaseAuth#exceptions_5
  private fun logInErrorMessage(exception: Exception?) =
      when (exception) {
        is FirebaseAuthInvalidUserException -> getString(R.string.error_email_doesnt_exist_or_disabled)
        is FirebaseAuthInvalidCredentialsException -> getString(R.string.error_invalid_password)
        else -> getString(R.string.general_sign_up_log_in_error)
      }
}
