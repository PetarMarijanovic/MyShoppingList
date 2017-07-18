package com.petarmarijanovic.myshoppinglist.screen.items

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingItem

/** Created by petar on 12/07/2017. */
class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
  
  private val items: MutableList<Identity<ShoppingItem>> = ArrayList()
  private var itemListener: ItemListener? = null
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_shopping_item, parent, false)
    
    return ViewHolder(view)
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.apply {
      val item = items[position].value
      bindChecked(item.checked)
      bindName(item.name)
      bindQuantity(item.quantity)
    }
  }
  
  override fun getItemCount() = items.size
  
  
  // TODO Should be update
  fun replace(item: Identity<ShoppingItem>) =
      items.filter { it.id == item.id }.firstOrNull()?.let {
        val index = items.indexOf(it)
        items[index] = item
        notifyItemChanged(index)
      }
  
  fun remove(item: Identity<ShoppingItem>) =
      items.filter { it.id == item.id }.firstOrNull()?.let {
        val index = items.indexOf(it)
        items.removeAt(index)
        notifyItemRemoved(index)
      }
  
  fun add(item: Identity<ShoppingItem>) {
    items.add(item)
    notifyItemInserted(items.size - 1)
  }
  
  fun registerItemListener(itemListener: ItemListener) {
    this.itemListener = itemListener
  }
  
  inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    
    private val checkbox = view.findViewById(R.id.checkbox) as CheckBox
    private val minus = view.findViewById(R.id.minus) as Button
    private val quantity = view.findViewById(R.id.quantity) as TextView
    private val plus = view.findViewById(R.id.plus) as Button
    
    init {
      checkbox.setOnCheckedChangeListener { _, isChecked ->
        itemListener?.checked(isChecked, items[layoutPosition])
      }
      minus.setOnClickListener { itemListener?.minus(items[layoutPosition]) }
      plus.setOnClickListener { itemListener?.plus(items[layoutPosition]) }
    }
    
    fun bindName(name: String) {
      this.checkbox.text = name
    }
    
    fun bindChecked(checked: Boolean) {
      if (checkbox.isChecked != checked) checkbox.isChecked = checked
    }
    
    fun bindQuantity(quantity: Int) {
      this.quantity.text = quantity.toString()
    }
  }
}

interface ItemListener {
  
  fun checked(isChecked: Boolean, item: Identity<ShoppingItem>)
  
  fun plus(item: Identity<ShoppingItem>)
  
  fun minus(item: Identity<ShoppingItem>)
  
}
