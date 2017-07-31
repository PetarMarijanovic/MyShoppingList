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
  
  private var items: MutableList<Identity<ShoppingList>> = ArrayList()
  private var clickListener: ((Identity<ShoppingList>) -> Unit)? = null
  
  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent?.context)
        .inflate(R.layout.list_shopping_list, parent, false)
    
    return ViewHolder(view)
  }
  
  override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    val list = items[position].value
    holder?.bindName(list.name + "\n" + list.users.toString() + "\n" + list.items.toString())
  }
  
  override fun getItemCount() = items.size
  
  fun add(item: Identity<ShoppingList>) {
    items.add(item)
    notifyItemInserted(items.size - 1)
  }
  
  fun update(item: Identity<ShoppingList>) =
      items.filter { it.id == item.id }.firstOrNull()?.let {
        val index = items.indexOf(it)
        items[index] = item
        notifyItemChanged(index)
      }
  
  fun remove(item: Identity<ShoppingList>) =
      items.filter { it.id == item.id }.firstOrNull()?.let {
        val index = items.indexOf(it)
        items.removeAt(index)
        notifyItemRemoved(index)
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
