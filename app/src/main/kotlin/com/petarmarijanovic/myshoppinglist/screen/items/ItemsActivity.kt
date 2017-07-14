package com.petarmarijanovic.myshoppinglist.screen.items

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.petarmarijanovic.myshoppinglist.AuthActivity
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingItem
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingList
import com.petarmarijanovic.myshoppinglist.data.repo.Repo
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingItemRepo
import com.petarmarijanovic.myshoppinglist.screen.lists.ItemsAdapter
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.screen_items.*
import timber.log.Timber
import javax.inject.Inject

class ItemsActivity : AuthActivity() {
  
  companion object {
    private const val KEY_LIST_ID = "key_list_id"
    
    fun intent(context: Context, listId: String) =
        Intent(context, ItemsActivity::class.java).apply { putExtra(KEY_LIST_ID, listId) }
  }
  
  @Inject
  lateinit var listRepo: Repo<ShoppingList>
  
  @Inject
  lateinit var itemsRepo: ShoppingItemRepo
  
  private val disposables = CompositeDisposable()
  private lateinit var listId: String
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
      registerClickListener({ startActivity(ItemsActivity.intent(context, it.id)) })
    }
    
    recycler_view.apply {
      layoutManager = LinearLayoutManager(context)
      setHasFixedSize(true)
      adapter = itemsAdapter
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
  
  private fun showAddItemDialog() {
    val view = layoutInflater.inflate(R.layout.dialog_add_item, null)
    val editText = view.findViewById(R.id.name) as EditText
    
    val listener: (DialogInterface, Int) -> Unit = { _, _ ->
      val name = editText.text.toString()
      saveList(if (name.isNotBlank()) name else getString(R.string.dialog_add_item_hint))
    }
    
    AlertDialog.Builder(this)
        .setView(view)
        .setPositiveButton(R.string.general_add, listener)
        .setNegativeButton(R.string.general_cancel, null)
        .show()
  }
  
  private fun saveList(name: String) {
    disposables.add(
        itemsRepo.insert(ShoppingItem(name, 1), listId)
            .subscribe({},
                       {
                         Timber.e(it, "Error while adding item")
                         Toast.makeText(this,
                                        "An error occurred. Try again later.",
                                        Toast.LENGTH_SHORT).show()
                       }))
  }
  
  override fun onStart() {
    super.onStart()
    disposables.add(listRepo.observe(listId)
                        .subscribe({ toolbar.title = it.value.name },
                                   { Timber.e(it, "Error while observing lists") }))
    
    disposables.add(itemsRepo.observe(listId)
                        .subscribe({ itemsAdapter.show(it) },
                                   { Timber.e(it, "Error while observing lists") })
    )
  }
  
  override fun onStop() {
    disposables.clear()
    super.onStop()
  }
}
