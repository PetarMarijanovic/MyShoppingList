package com.petarmarijanovic.myshoppinglist.screen.items

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.EditText
import com.petarmarijanovic.myshoppinglist.AuthActivity
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingItem
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingList
import com.petarmarijanovic.myshoppinglist.data.repo.Repo
import dagger.android.AndroidInjection
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
  lateinit var listRepo: Repo<ShoppingList>
  
  private val disposables = CompositeDisposable()
  private var listId: String? = null
  private lateinit var itemsAdapter: ItemsAdapter
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AndroidInjection.inject(this)
    setContentView(R.layout.screen_items)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    
    listId = intent.getStringExtra(KEY_LIST_ID)
    
    val context = this
    itemsAdapter = ItemsAdapter().apply {
      registerItemListener(object : ItemListener {
        override fun toggle(position: Int, item: ShoppingItem) {
          notifyItemChanged(position, ItemsAdapter.Payload.TOGGLE)
        }
        
        override fun plus(position: Int, item: ShoppingItem) {
          notifyItemChanged(position, ItemsAdapter.Payload.PLUS)
        }
        
        override fun minus(position: Int, item: ShoppingItem) {
          notifyItemChanged(position, ItemsAdapter.Payload.MINUS)
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
  
  // TODO For now
  private fun showAddItemDialog() {
    val view = layoutInflater.inflate(R.layout.dialog_add_item, null)
    val editText = view.findViewById(R.id.name) as EditText
    
    val listener: (DialogInterface, Int) -> Unit = { _, _ ->
      val name = editText.text.toString()
      itemsAdapter.add(ShoppingItem(false, name, 1))
    }
    
    AlertDialog.Builder(this)
        .setView(view)
        .setPositiveButton(R.string.general_add, listener)
        .setNegativeButton(R.string.general_cancel, null)
        .show()
  }
  
  override fun onStart() {
    super.onStart()
    listId?.let {
      disposables.add(
          listRepo.observe(it)
              .firstElement()
              .map { it.value }
              .subscribe({
                           name.setText(it.name)
                           it.shoppingItems?.let { itemsAdapter.show(it.toMutableList()) }
                         },
                         { Timber.e(it, "Error while observing lists") }))
    }
  }
  
  override fun onStop() {
    // TODO Insert or Update
    listRepo.insert(ShoppingList(name.text.toString(), itemsAdapter.items)).subscribe()
    disposables.clear()
    super.onStop()
  }
}
