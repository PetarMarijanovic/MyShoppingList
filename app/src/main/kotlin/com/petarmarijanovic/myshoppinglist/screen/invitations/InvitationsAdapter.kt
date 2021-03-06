package com.petarmarijanovic.myshoppinglist.screen.invitations

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.Invitation
import com.petarmarijanovic.myshoppinglist.extensions.addPaddingForItems
import com.petarmarijanovic.myshoppinglist.extensions.reuseViewHolder
import com.petarmarijanovic.myshoppinglist.screen.items.dpToPx
import kotlinx.android.synthetic.main.list_invitation.view.*
import java.util.*

/** Created by petar on 12/07/2017. */
class InvitationsAdapter : RecyclerView.Adapter<InvitationsAdapter.ViewHolder>() {
  
  private var items: MutableList<Identity<Invitation>> = ArrayList()
  private var invitationListener: InvitationListener? = null
  
  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
      ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_invitation,
                                                              parent, false))
  
  override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    val list = items[position].value
    holder?.listName(list.listName)
  }
  
  override fun getItemCount() = items.size
  
  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    recyclerView.apply {
      reuseViewHolder()
      addPaddingForItems(dpToPx(8f).toInt())
    }
  }
  
  fun clear() {
    val size = items.size
    items.clear()
    notifyItemRangeRemoved(0, size)
  }
  
  fun add(items: List<Identity<Invitation>>) {
    this.items.clear()
    this.items.addAll(items)
    notifyDataSetChanged()
  }
  
  fun registerListener(listener: InvitationListener) {
    this.invitationListener = listener
  }
  
  inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    
    private val listName = view.findViewById(R.id.list_name) as TextView
    
    init {
      view.setOnClickListener(this)
      view.accept.setOnClickListener { invitationListener?.accept(items[layoutPosition]) }
      view.decline.setOnClickListener { invitationListener?.decline(items[layoutPosition]) }
    }
    
    fun listName(name: String) {
      listName.text = name
    }
    
    override fun onClick(v: View) {
      //      userListener?.click(items[layoutPosition])
    }
  }
}

interface InvitationListener {
  
  fun accept(invitation: Identity<Invitation>)
  
  fun decline(invitation: Identity<Invitation>)
  
}
