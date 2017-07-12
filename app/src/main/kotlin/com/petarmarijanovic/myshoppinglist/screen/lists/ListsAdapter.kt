package com.petarmarijanovic.myshoppinglist.screen.lists

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingList
import java.util.*

/** Created by petar on 12/07/2017. */
class ListsAdapter : RecyclerView.Adapter<ListsAdapter.ViewHolder>() {
  
  private var items = Collections.emptyList<Identity<ShoppingList>>()
  private var clickListener: ((Identity<ShoppingList>) -> Unit)? = null
  
  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent?.context)
        .inflate(R.layout.list_item_shopping_list, parent, false)
    
    return ViewHolder(view)
  }
  
  override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    val list = items[position].value
    holder?.bindName(list.name)
  }
  
  override fun getItemCount() = items.size
  
  fun show(items: List<Identity<ShoppingList>>) {
    this.items = items
    notifyDataSetChanged()
  }
  
  fun registerClickListener(clickListener: (Identity<ShoppingList>) -> Unit) {
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
