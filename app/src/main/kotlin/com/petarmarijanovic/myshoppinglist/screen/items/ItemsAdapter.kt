package com.petarmarijanovic.myshoppinglist.screen.lists

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
import java.util.*

/** Created by petar on 12/07/2017. */
class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
  
  private var items = Collections.emptyList<Identity<ShoppingItem>>()
  private var itemListener: ItemListener? = null
  
  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent?.context)
        .inflate(R.layout.list_shopping_item, parent, false)
    
    return ViewHolder(view)
  }
  
  override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    val item = items[position].value
    holder?.bindChecked(item.checked)
    holder?.bindName(item.name)
    holder?.bindQuantity(item.quantity)
  }
  
  override fun getItemCount() = items.size
  
  fun show(items: List<Identity<ShoppingItem>>) {
    this.items = items
    notifyDataSetChanged()
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
        val item = items[layoutPosition]
        if (item.value.checked != isChecked) itemListener?.toggle(item)
      }
      minus.setOnClickListener { itemListener?.minus(items[layoutPosition]) }
      plus.setOnClickListener { itemListener?.plus(items[layoutPosition]) }
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
  
  fun toggle(item: Identity<ShoppingItem>)
  
  fun plus(item: Identity<ShoppingItem>)
  
  fun minus(item: Identity<ShoppingItem>)
  
}
