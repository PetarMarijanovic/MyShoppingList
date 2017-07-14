package com.petarmarijanovic.myshoppinglist.screen.lists

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingItem
import java.util.*

/** Created by petar on 12/07/2017. */
class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
  
  private var items = Collections.emptyList<Identity<ShoppingItem>>()
  private var clickListener: ((Identity<ShoppingItem>) -> Unit)? = null
  
  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent?.context)
        .inflate(R.layout.list_shopping_item, parent, false)
    
    return ViewHolder(view)
  }
  
  override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    val item = items[position].value
    holder?.bindName(item.name)
  }
  
  override fun getItemCount() = items.size
  
  fun show(items: List<Identity<ShoppingItem>>) {
    this.items = items
    notifyDataSetChanged()
  }
  
  fun registerClickListener(clickListener: (Identity<ShoppingItem>) -> Unit) {
    this.clickListener = clickListener
  }
  
  inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    
    private val name = view.findViewById(R.id.name) as TextView
    
    init {
      view.setOnClickListener(this)
    }
    
    fun bindName(name: String) {
      this.name.text = name
    }
    
    override fun onClick(v: View) {
      clickListener?.invoke(items[layoutPosition])
    }
  }
}
