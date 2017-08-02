package com.petarmarijanovic.myshoppinglist.screen.invitations

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.application.MyShoppingListApplication
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.Invitation
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingListRepo
import com.petarmarijanovic.myshoppinglist.screen.AuthActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.screen_users.*
import javax.inject.Inject

class InvitationsActivity : AuthActivity() {
  
  @Inject
  lateinit var listRepo: ShoppingListRepo
  
  private val subscriptions = CompositeDisposable()
  private lateinit var invitationsAdapter: InvitationsAdapter
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MyShoppingListApplication).userComponent?.inject(this)
    setContentView(R.layout.screen_invitations)
    setSupportActionBar(toolbar)
    
    invitationsAdapter = InvitationsAdapter().apply {
      registerListener(object : InvitationListener {
        override fun accept(invitation: Identity<Invitation>) {
          //          listRepo.acceptInvitation(invitation)
        }
        
        override fun decline(invitation: Identity<Invitation>) {
          //          listRepo.declineInvitation(invitation)
        }
        
      })
    }
    val context = this
    recycler_view.apply {
      adapter = invitationsAdapter
      layoutManager = LinearLayoutManager(context)
      setHasFixedSize(true)
    }
  }
  
  override fun onStart() {
    super.onStart()
    //    subscriptions.add(listRepo.invitations()
    //                          .subscribe({ invitationsAdapter.add(it) },
    //                                     { Timber.e(it, "Error while observing invitations") }))
  }
  
  override fun onStop() {
    subscriptions.clear()
    super.onStop()
  }
}
