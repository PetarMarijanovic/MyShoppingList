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
  
  // TODO Avoid MutableList?
  var items: MutableList<Identity<ShoppingItem>> = ArrayList()
  private var itemListener: ItemListener? = null
  
  public fun indexOf(item: Identity<ShoppingItem>): Int {
    return items.indexOf(items.filter { it.id == item.id }.firstOrNull()!!)
  }
  
  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent?.context)
        .inflate(R.layout.list_shopping_item, parent, false)
    
    return ViewHolder(view)
  }
  
  override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    holder?.apply {
      val item = items[position].value
      bindChecked(item.checked)
      bindName(item.name)
      bindQuantity(item.quantity)
    }
  }
  
  override fun getItemCount() = items.size
  
  fun show(items: MutableList<Identity<ShoppingItem>>) {
    this.items = items
    notifyDataSetChanged()
  }
  
  // TODO Remove nullable
  fun add(item: Identity<ShoppingItem>?) {
    if (item == null) return
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
        // TODO This is only a quick fix
        val item = items[layoutPosition].value
        if (item.checked != isChecked) itemListener?.toggle(layoutPosition, items[layoutPosition])
      }
      minus.setOnClickListener { itemListener?.minus(layoutPosition, items[layoutPosition]) }
      plus.setOnClickListener { itemListener?.plus(layoutPosition, items[layoutPosition]) }
    }
    
    fun bindName(name: String) {
      this.checkbox.text = name
    }
    
    fun bindChecked(checked: Boolean) {
      checkbox.isChecked = checked
    }
    
    fun bindQuantity(quantity: Int) {
      this.quantity.text = quantity.toString()
    }
  }
}

interface ItemListener {
  
  fun toggle(position: Int, item: Identity<ShoppingItem>)
  
  fun plus(position: Int, item: Identity<ShoppingItem>)
  
  fun minus(position: Int, item: Identity<ShoppingItem>)
  
}
