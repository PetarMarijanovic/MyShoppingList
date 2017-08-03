package com.petarmarijanovic.myshoppinglist.screen.lists

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingList
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingListRepo
import com.petarmarijanovic.myshoppinglist.screen.AuthActivity
import com.petarmarijanovic.myshoppinglist.screen.invitations.InvitationsActivity
import com.petarmarijanovic.myshoppinglist.screen.items.ItemsActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.screen_lists.*
import timber.log.Timber
import javax.inject.Inject

class ListsActivity : AuthActivity() {
  
  @Inject
  lateinit var listRepo: ShoppingListRepo
  
  private val disposables = CompositeDisposable()
  private lateinit var listsAdapter: ListsAdapter
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MyShoppingListApplication).userComponent?.inject(this)
    setContentView(R.layout.screen_lists)
    setSupportActionBar(toolbar)
    
    val context = this
    listsAdapter = ListsAdapter().apply {
      registerClickListener(object : ListListener {
        override fun click(list: Identity<ShoppingList>) {
          startActivity(ItemsActivity.intent(context, list.id))
        }
        
        override fun swiped(list: Identity<ShoppingList>) {
          listRepo.remove(list.id)
        }
      })
    }
    
    recycler_view.apply {
      adapter = listsAdapter
      layoutManager = LinearLayoutManager(context)
      setHasFixedSize(true)
    }
    
    fab.setOnClickListener {
      startActivity(ItemsActivity.intent(context, listRepo.add("New List")))
    }
  }
  
  override fun onStart() {
    super.onStart()
    listsAdapter.clear()
    // TODO THIS IS NOT GOOOOOOODDDDD!!!!!!! WHAT ABOUT UPDATE
    disposables.add(listRepo.observeLists()
                        .subscribe({ listsAdapter.addAll(it) },
                                   { Timber.e(it, "Error while observing lists") }))
  }
  
  override fun onStop() {
    disposables.clear()
    super.onStop()
  }
  
  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_lists, menu)
    return true
  }
  
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.action_settings -> {
        FirebaseAuth.getInstance().signOut()
        return true
      }
      R.id.action_invitations -> {
        startActivity(Intent(this, InvitationsActivity::class.java))
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }
}
