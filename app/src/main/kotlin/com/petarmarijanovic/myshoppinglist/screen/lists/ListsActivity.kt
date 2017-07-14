package com.petarmarijanovic.myshoppinglist.screen.lists

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.petarmarijanovic.myshoppinglist.AuthActivity
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingList
import com.petarmarijanovic.myshoppinglist.data.repo.Repo
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.screen_lists.*
import timber.log.Timber
import javax.inject.Inject

class ListsActivity : AuthActivity() {
  
  @Inject
  lateinit var listRepo: Repo<ShoppingList>
  
  private val disposables = CompositeDisposable()
  private lateinit var listsAdapter: ListsAdapter
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AndroidInjection.inject(this)
    setContentView(R.layout.screen_lists)
    setSupportActionBar(toolbar)
    
    listsAdapter = ListsAdapter().apply { registerClickListener({ Timber.d(it.id) }) }
    
    recycler_view.apply {
      layoutManager = LinearLayoutManager(context)
      setHasFixedSize(true)
      adapter = listsAdapter
    }
    
    fab.setOnClickListener { showCreateListDialog() }
  }
  
  override fun onStart() {
    super.onStart()
    disposables.add(listRepo.observe().subscribe({ listsAdapter.show(it) },
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
    // Handle item selection
    when (item.itemId) {
      R.id.action_settings -> {
        FirebaseAuth.getInstance().signOut()
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }
  
  private fun showCreateListDialog() {
    val view = layoutInflater.inflate(R.layout.dialog_new_list, null)
    val editText = view.findViewById(R.id.name) as EditText
    
    val listener: (DialogInterface, Int) -> Unit = { _, _ ->
      val name = editText.text.toString()
      saveList(if (name.isNotBlank()) name else getString(R.string.dialog_new_list_hint))
    }
    
    AlertDialog.Builder(this)
        .setView(view)
        .setPositiveButton(R.string.general_add, listener)
        .setNegativeButton(R.string.general_cancel, null)
        .show()
  }
  
  private fun saveList(name: String) {
    disposables.add(
        listRepo.insert(ShoppingList(name))
            .subscribe({},
                       {
                         Timber.e(it, "Error while saving list")
                         Toast.makeText(this,
                                        "An error occurred. Try again later.",
                                        Toast.LENGTH_SHORT).show()
                       }))
  }
}
