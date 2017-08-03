package com.petarmarijanovic.myshoppinglist.screen.lists

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.data.Event
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
  
  private val listsAdapter = ListsAdapter().apply {
    registerClickListener(object : ListsListener {
      override fun onClick(list: Identity<ShoppingList>) {
        startActivity(ItemsActivity.intent(this@ListsActivity, list.id))
      }
      
      override fun onSwipe(list: Identity<ShoppingList>) {
        listRepo.remove(list.id, list.value.users.size == 1)
      }
    })
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MyShoppingListApplication).userComponent?.inject(this)
    setContentView(R.layout.screen_lists)
    setSupportActionBar(toolbar)
    
    recycler_view.apply {
      adapter = listsAdapter
      layoutManager = LinearLayoutManager(context)
      setHasFixedSize(true)
    }
    
    fab.setOnClickListener {
      startActivity(
          ItemsActivity.intent(this@ListsActivity, listRepo.add(getString(R.string.new_list))))
    }
  }
  
  override fun onStart() {
    super.onStart()
    disposables.add(observeLists())
  }
  
  override fun onStop() {
    disposables.clear()
    listsAdapter.clear()
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
  
  private fun observeLists() =
      listRepo.observeLists()
          .subscribe({
                       when (it.event) {
                         Event.ADD -> listsAdapter.add(it.item)
                         Event.UPDATE -> listsAdapter.update(it.item)
                         Event.REMOVE -> listsAdapter.remove(it.item.id)
                       }
                     },
                     { Timber.e(it, "Error while observing lists") })
}
