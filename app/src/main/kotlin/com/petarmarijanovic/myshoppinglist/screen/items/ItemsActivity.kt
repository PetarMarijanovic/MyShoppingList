package com.petarmarijanovic.myshoppinglist.screen.items

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.data.Event
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingItem
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingListRepo
import com.petarmarijanovic.myshoppinglist.screen.AuthActivity
import com.petarmarijanovic.myshoppinglist.screen.users.UsersActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.screen_items.*
import timber.log.Timber
import javax.inject.Inject

class ItemsActivity : AuthActivity() {
  
  companion object {
    private const val KEY_LIST_ID = "key_list_id"
    
    fun intent(context: Context, listId: String? = null) =
        Intent(context, ItemsActivity::class.java)
            .apply { listId?.let { putExtra(KEY_LIST_ID, it) } }
  }
  
  @Inject
  lateinit var listRepo: ShoppingListRepo
  
  private lateinit var listId: String
  private lateinit var itemsAdapter: ItemsAdapter
  private val disposables = CompositeDisposable()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MyShoppingListApplication).userComponent?.inject(this)
    setContentView(R.layout.screen_items)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    
    listId = intent.getStringExtra(KEY_LIST_ID)
    
    itemsAdapter = ItemsAdapter().apply {
      registerItemListener(object : ItemListener {
        override fun nameFocusLost(name: String, item: Identity<ShoppingItem>) {
          if (name != item.value.name) {
            listRepo.updateItem(listId, item.id, "name", name)
          }
        }
        
        override fun swiped(item: Identity<ShoppingItem>) {
          listRepo.deleteItem(listId, item.id)
        }
        
        override fun checked(isChecked: Boolean, item: Identity<ShoppingItem>) {
          listRepo.updateItem(listId, item.id, "checked", isChecked)
        }
        
        override fun plus(item: Identity<ShoppingItem>) {
          listRepo.updateItem(listId, item.id, "quantity", item.value.quantity + 1)
        }
        
        override fun minus(item: Identity<ShoppingItem>) {
          val quantity = item.value.quantity - 1
          if (quantity > 0) listRepo.updateItem(listId, item.id, "quantity", quantity)
        }
      })
    }
    
    recycler_view.apply {
      adapter = itemsAdapter
      layoutManager = LinearLayoutManager(context)
      setHasFixedSize(true)
    }
    
    fab.setOnClickListener { listRepo.addItem(listId, ShoppingItem(false, "abs", 1)) }
  }
  
  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_items, menu)
    return true
  }
  
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        finish()
        return true
      }
      R.id.action_users -> {
        startActivity(UsersActivity.intent(this, listId))
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }
  
  override fun onStart() {
    super.onStart()
    itemsAdapter.clear()
    disposables.add(listRepo.name(listId)
                        .subscribe({ if (it.isDefined()) name.setText(it.get()) },
                                   { Timber.e(it, "Error while observing list name") }))
    
    disposables.add(listRepo.items(listId)
                        .subscribe({
                                     when (it.event) {
                                       Event.ADD -> itemsAdapter.add(it.item)
                                       Event.UPDATE -> itemsAdapter.update(it.item)
                                       Event.REMOVE -> itemsAdapter.remove(it.item)
                                     }
                                   },
                                   { Timber.e(it, "Error while observing items") }))
  }
  
  override fun onStop() {
    if (name.text.isNotBlank()) listRepo.updateName(listId, name.text.toString())
    
    disposables.clear()
    super.onStop()
  }
}
