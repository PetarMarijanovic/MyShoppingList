package com.petarmarijanovic.myshoppinglist.screen.items

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.EditText
import com.androidhuman.rxfirebase2.database.*
import com.google.firebase.database.FirebaseDatabase
import com.petarmarijanovic.myshoppinglist.AuthActivity
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingItem
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.screen_items.*
import timber.log.Timber

class ItemsActivity : AuthActivity() {
  
  companion object {
    private const val KEY_LIST_ID = "key_list_id"
    
    fun intent(context: Context, listId: String? = null) =
        Intent(context, ItemsActivity::class.java)
            .apply { listId?.let { putExtra(KEY_LIST_ID, it) } }
  }
  
  private var listId: String? = null
  private lateinit var itemsAdapter: ItemsAdapter
  private val disposables = CompositeDisposable()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.screen_items)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    
    listId = intent.getStringExtra(KEY_LIST_ID)
    
    itemsAdapter = ItemsAdapter().apply {
      registerItemListener(object : ItemListener {
        override fun nameFocusLost(name: String, item: Identity<ShoppingItem>) {
          if (name != item.value.name) ref().rxUpdateChildren(mapOf("/${item.id}/name" to name)).subscribe()
        }
        
        override fun swiped(item: Identity<ShoppingItem>) {
          ref().child(item.id).rxRemoveValue().subscribe()
        }
        
        override fun checked(isChecked: Boolean, item: Identity<ShoppingItem>) {
          ref().rxUpdateChildren(mapOf("/${item.id}/checked" to isChecked)).subscribe()
        }
        
        override fun plus(item: Identity<ShoppingItem>) {
          ref().rxUpdateChildren(mapOf("/${item.id}/quantity" to (item.value.quantity + 1))).subscribe()
        }
        
        override fun minus(item: Identity<ShoppingItem>) {
          val quantity = item.value.quantity - 1
          if (quantity > 0) ref().rxUpdateChildren(mapOf("/${item.id}/quantity" to quantity)).subscribe()
        }
      })
    }
    
    recycler_view.apply {
      adapter = itemsAdapter
      layoutManager = LinearLayoutManager(context)
      setHasFixedSize(true)
    }
    
    fab.setOnClickListener { showAddItemDialog() }
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
  
  private val firebaseDatabase = FirebaseDatabase.getInstance()
  
  private fun ref() = firebaseDatabase.getReference("shopping_items").child(listId)
  
  // TODO For now
  private fun showAddItemDialog() {
    val view = layoutInflater.inflate(R.layout.dialog_add_item, null)
    val editText = view.findViewById(R.id.name) as EditText
    
    val listener: (DialogInterface, Int) -> Unit = { _, _ ->
      ref().push().rxSetValue(ShoppingItem(false, editText.text.toString(), 1)).subscribe()
    }
    
    AlertDialog.Builder(this)
        .setView(view)
        .setPositiveButton(R.string.general_add, listener)
        .setNegativeButton(R.string.general_cancel, null)
        .show()
  }
  
  override fun onStart() {
    super.onStart()
    disposables.add(
        ref()
            .rxChildEvents()
            .subscribe({
                         val item = Identity.fromSnapshot(it.dataSnapshot(),
                                                          ShoppingItem::class.java)
                         when (it) {
                           is ChildAddEvent -> itemsAdapter.add(item)
                           is ChildChangeEvent -> itemsAdapter.replace(item)
                           is ChildMoveEvent -> throw IllegalArgumentException(it.toString() + " move not supported")
                           is ChildRemoveEvent -> itemsAdapter.remove(item)
                           else -> throw IllegalArgumentException(it.toString() + " not supported")
                         }
                       },
                       { Timber.e(it, "Error while observing lists") }))
  }
  
  override fun onStop() {
    disposables.clear()
    super.onStop()
  }
}
