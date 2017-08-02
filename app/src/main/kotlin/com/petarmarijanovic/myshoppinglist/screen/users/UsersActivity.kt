package com.petarmarijanovic.myshoppinglist.screen.users

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.data.Event
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingListRepo
import com.petarmarijanovic.myshoppinglist.screen.AuthActivity
import com.petarmarijanovic.myshoppinglist.screen.lists.UserListener
import com.petarmarijanovic.myshoppinglist.screen.lists.UsersAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.screen_users.*
import timber.log.Timber
import javax.inject.Inject

class UsersActivity : AuthActivity() {
  
  companion object {
    private const val KEY_LIST_ID = "key_list_id"
    private const val KEY_LIST_NAME = "key_list_name"
    
    fun intent(context: Context, listId: String, listName: String) =
        Intent(context, UsersActivity::class.java)
            .apply {
              putExtra(KEY_LIST_ID, listId)
              putExtra(KEY_LIST_NAME, listName)
            }
  }
  
  @Inject
  lateinit var listRepo: ShoppingListRepo
  
  private val subscriptions = CompositeDisposable()
  private lateinit var usersAdapter: UsersAdapter
  private lateinit var listId: String
  private lateinit var listName: String
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MyShoppingListApplication).userComponent?.inject(this)
    setContentView(R.layout.screen_users)
    setSupportActionBar(toolbar)
    
    listId = intent.getStringExtra(UsersActivity.KEY_LIST_ID)
    listName = intent.getStringExtra(UsersActivity.KEY_LIST_NAME)
    
    usersAdapter = UsersAdapter().apply {
      registerListener(object : UserListener {
        override fun removed(email: String) {
          //          listRepo.deleteUser(listId, user.id)
        }
        
      })
    }
    val context = this
    recycler_view.apply {
      adapter = usersAdapter
      layoutManager = LinearLayoutManager(context)
      setHasFixedSize(true)
    }
    
    fab.setOnClickListener {
      //      listRepo.sendInvitation("petar2@test.com",
      //                              Invitation(user.value.email, listId, listName))
    }
  }
  
  override fun onStart() {
    super.onStart()
    subscriptions.add(listRepo.usersPerListDataChanges(listId)
                          .subscribe({
                                       when (it.event) {
                                         Event.ADD -> usersAdapter.add(it.item)
                                         Event.UPDATE -> usersAdapter.update(it.item)
                                         Event.REMOVE -> usersAdapter.remove(it.item)
                                       }
                                     },
                                     { Timber.e(it, "Error while observing users") }))
  }
  
  override fun onStop() {
    subscriptions.clear()
    super.onStop()
  }
}
