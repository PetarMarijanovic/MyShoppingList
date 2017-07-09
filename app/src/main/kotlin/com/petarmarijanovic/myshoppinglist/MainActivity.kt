package com.petarmarijanovic.myshoppinglist

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AuthActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    logout.setOnClickListener { FirebaseAuth.getInstance().signOut() }
  }
}
