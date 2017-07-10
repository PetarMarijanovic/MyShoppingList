package com.petarmarijanovic.myshoppinglist

import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.petarmarijanovic.myshoppinglist.rxfirebase.Identity
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AuthActivity() {
  
  @Inject
  lateinit var shoppingListRepo: Repo<ShoppingList>
  
  private val subscription = CompositeDisposable()
  
  private val list = ArrayList<Identity<ShoppingList>>()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AndroidInjection.inject(this)
    setContentView(R.layout.activity_main)
    logout.setOnClickListener { FirebaseAuth.getInstance().signOut() }
    
    val shoppingList = ShoppingList("TestList")
    save.setOnClickListener { subscription.add(shoppingListRepo.insert(shoppingList).subscribe()) }
    
    update.setOnClickListener {
      subscription.add(
          shoppingListRepo.update(
              Identity(list[0].id, list[0].value.copy(name = "Vuco"))).subscribe())
    }
    
    delete.setOnClickListener { subscription.add(shoppingListRepo.remove(list[0].id).subscribe()) }
  }
  
  override fun onStart() {
    super.onStart()
    
    subscription.add(shoppingListRepo.observe()
                         .subscribe({
                                      observe.text = it.toString()
                                      list.clear()
                                      list.addAll(it)
                                    },
                                    { Log.e("Petarr", it.message) }))
  }
  
  override fun onStop() {
    subscription.clear()
    super.onStop()
  }
}
