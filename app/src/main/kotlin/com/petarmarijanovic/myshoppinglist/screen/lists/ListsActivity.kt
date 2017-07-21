package com.petarmarijanovic.myshoppinglist.screen.lists

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.androidhuman.rxfirebase2.database.dataChanges
import com.androidhuman.rxfirebase2.database.rxSetValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.AuthActivity
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingList
import com.petarmarijanovic.myshoppinglist.screen.items.ItemsActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.screen_lists.*
import timber.log.Timber
import javax.inject.Inject

class ListsActivity : AuthActivity() {
  
  @Inject
  lateinit var firebaseDatabase: FirebaseDatabase
  
  private val disposables = CompositeDisposable()
  private lateinit var listsAdapter: ListsAdapter
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MyShoppingListApplication).applicationComponent.inject(this)
    setContentView(R.layout.screen_lists)
    setSupportActionBar(toolbar)
    
    val context = this
    listsAdapter = ListsAdapter().apply {
      registerClickListener({ startActivity(ItemsActivity.intent(context, it.id)) })
    }
    
    recycler_view.apply {
      adapter = listsAdapter
      layoutManager = LinearLayoutManager(context)
      setHasFixedSize(true)
    }
    
    fab.setOnClickListener {
      val ref = firebaseDatabase.getReference("shopping_list").child(FirebaseAuth.getInstance().currentUser?.uid).push()
      ref.rxSetValue(ShoppingList(""))
          .subscribe({ startActivity(ItemsActivity.intent(context, ref.key)) })
    }
  }
  
  override fun onStart() {
    super.onStart()
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "for now"
    disposables.add(firebaseDatabase.getReference("shopping_list").child(uid)
                        .dataChanges()
                        .map { it.children }
                        .map {
                          it.map {
                            Identity(it.ref.key, it.getValue(ShoppingList::class.java)!!)
                          }.toList()
                        }
                        .subscribe({ listsAdapter.show(it) },
                                   { Timber.e(it, "Error while observing lists") })
    )
  }
  
  override fun onStop() {
    disposables.clear()
    super.onStop()
  }
  
  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.lists_menu, menu)
    return true
  }
  
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.action_settings -> {
        FirebaseAuth.getInstance().signOut()
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }
}
