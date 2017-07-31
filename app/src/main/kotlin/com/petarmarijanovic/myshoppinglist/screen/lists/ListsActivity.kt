package com.petarmarijanovic.myshoppinglist.screen.lists

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.data.Event
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingList
import com.petarmarijanovic.myshoppinglist.data.model.User
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingListRepo
import com.petarmarijanovic.myshoppinglist.screen.AuthActivity
import com.petarmarijanovic.myshoppinglist.screen.items.ItemsActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.screen_lists.*
import timber.log.Timber
import javax.inject.Inject

class ListsActivity : AuthActivity() {
  
  @Inject
  lateinit var user: Identity<User>
  
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
          listRepo.deleteList(list)
        }
      })
    }
    
    recycler_view.apply {
      adapter = listsAdapter
      layoutManager = LinearLayoutManager(context)
      setHasFixedSize(true)
    }
    
    fab.setOnClickListener {
      startActivity(ItemsActivity.intent(context, listRepo.newList()))
    }
  }
  
  override fun onStart() {
    super.onStart()
    listsAdapter.clear()
    disposables.add(listRepo.lists()
                        .subscribe({
                                     when (it.event) {
                                       Event.ADD -> listsAdapter.add(it.item)
                                       Event.UPDATE -> listsAdapter.update(it.item)
                                       Event.REMOVE -> listsAdapter.remove(it.item)
                                     }
                                   },
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
      else -> return super.onOptionsItemSelected(item)
    }
  }
}
