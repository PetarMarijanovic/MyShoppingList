package com.petarmarijanovic.myshoppinglist.screen.users

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.extensions.addPaddingForItems
import com.petarmarijanovic.myshoppinglist.extensions.reuseViewHolder
import com.petarmarijanovic.myshoppinglist.screen.items.dpToPx
import java.util.*

/** Created by petar on 12/07/2017. */
class UsersAdapter : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
  
  private var emails: MutableList<String> = ArrayList()
  private var userListener: UserListener? = null
  
  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
      ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_user, parent, false))
  
  override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    emails[position].let { holder?.email(it) }
  }
  
  override fun getItemCount() = emails.size
  
  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    recyclerView.apply {
      reuseViewHolder()
      addPaddingForItems(dpToPx(8f).toInt())
    }
  }
  
  fun clear() {
    val size = emails.size
    emails.clear()
    notifyItemRangeRemoved(0, size)
  }
  
  fun add(email: String) {
    emails.add(email)
    notifyItemInserted(emails.size - 1)
  }
  
  fun update(email: String) =
      emails.filter { it == email }.firstOrNull()?.let {
        val index = emails.indexOf(it)
        emails[index] = email
        notifyItemChanged(index)
      }
  
  fun remove(email: String) =
      emails.filter { it == email }.firstOrNull()?.let {
        val index = emails.indexOf(it)
        emails.removeAt(index)
        notifyItemRemoved(index)
      }
  
  fun registerListener(listener: UserListener) {
    this.userListener = listener
  }
  
  inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    
    private val email = view.findViewById(R.id.email) as TextView
    
    init {
      view.setOnClickListener(this)
    }
    
    fun email(email: String) {
      this.email.text = email
    }
    
    override fun onClick(v: View) {
      //      userListener?.click(items[layoutPosition])
    }
  }
}

interface UserListener {
  
  fun removed(email: String)
  
}
