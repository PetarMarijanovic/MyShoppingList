package com.petarmarijanovic.myshoppinglist

import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.rxfirebase.Identity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AuthActivity() {
  
  private val ref = FirebaseDatabase.getInstance().getReference("shopping_list")
  private val shoppingListRepo = Repo(ref, ShoppingList::class.java)
  
  private val subscription = CompositeDisposable()
  
  private val list = ArrayList<Identity<ShoppingList>>()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
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
