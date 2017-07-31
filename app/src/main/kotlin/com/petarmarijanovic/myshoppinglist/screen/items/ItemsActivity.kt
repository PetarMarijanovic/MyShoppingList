package com.petarmarijanovic.myshoppinglist.screen.items

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingItem
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingListRepo
import com.petarmarijanovic.myshoppinglist.screen.AuthActivity
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
            //            itemRepo.update(listId, item.id, "name", name).subscribe()
          }
        }
        
        override fun swiped(item: Identity<ShoppingItem>) {
          //          itemRepo.delete(listId, item.id).subscribe()
        }
        
        override fun checked(isChecked: Boolean, item: Identity<ShoppingItem>) {
          //          itemRepo.update(listId, item.id, "checked", isChecked).subscribe()
        }
        
        override fun plus(item: Identity<ShoppingItem>) {
          //          itemRepo.update(listId, item.id, "quantity", item.value.quantity + 1).subscribe()
        }
        
        override fun minus(item: Identity<ShoppingItem>) {
          val quantity = item.value.quantity - 1
          //          if (quantity > 0) itemRepo.update(listId, item.id, "quantity", quantity).subscribe()
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
  
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        finish()
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }
  
  override fun onStart() {
    super.onStart()
    disposables.add(listRepo.name(listId)
                        .subscribe({ if (it.isDefined()) name.setText(it.get()) },
                                   { Timber.e(it, "Error while observing list name") }))
    
    //    disposables.add(itemRepo.lists(listId)
    //                        .subscribe({
    //                                     when (it.event) {
    //                                       Event.ADD -> itemsAdapter.add(it.item)
    //                                       Event.UPDATE -> itemsAdapter.update(it.item)
    //                                       Event.REMOVE -> itemsAdapter.remove(it.item)
    //                                     }
    //                                   },
    //                                   { Timber.e(it, "Error while observing items") }))
  }
  
  override fun onStop() {
    if (name.text.isNotBlank()) listRepo.updateName(listId, name.text.toString())
    
    disposables.clear()
    super.onStop()
  }
}
