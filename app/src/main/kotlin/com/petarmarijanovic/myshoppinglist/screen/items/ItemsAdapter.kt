package com.petarmarijanovic.myshoppinglist.screen.items

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingItem
import com.petarmarijanovic.myshoppinglist.extensions.addPaddingForItems
import com.petarmarijanovic.myshoppinglist.extensions.attachLeftRightSwipeAnimator
import com.petarmarijanovic.myshoppinglist.extensions.reuseViewHolder

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
  
  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    recyclerView.apply {
      reuseViewHolder()
      addPaddingForItems(dpToPx(8f).toInt())
      attachLeftRightSwipeAnimator { itemListener?.swiped(items[it]) }
    }
  }
  
  fun clear() {
    val size = items.size
    items.clear()
    notifyItemRangeRemoved(0, size)
  }
  
  fun add(item: Identity<ShoppingItem>) {
    items.add(item)
    notifyItemInserted(items.size - 1)
  }
  
  fun update(item: Identity<ShoppingItem>) =
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
  
  fun registerItemListener(itemListener: ItemListener) {
    this.itemListener = itemListener
  }
  
  inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    
    private val checkbox = view.findViewById(R.id.checkbox) as CheckBox
    private val name = view.findViewById(R.id.name) as EditText
    private val minus = view.findViewById(R.id.minus) as Button
    private val quantity = view.findViewById(R.id.quantity) as TextView
    private val plus = view.findViewById(R.id.plus) as Button
    
    init {
      checkbox.setOnCheckedChangeListener { _, isChecked ->
        name.clearFocus()
        itemListener?.checked(isChecked, items[layoutPosition])
      }
      
      minus.setOnClickListener {
        name.clearFocus()
        itemListener?.minus(items[layoutPosition])
      }
      
      plus.setOnClickListener {
        name.clearFocus()
        itemListener?.plus(items[layoutPosition])
      }
      
      // layoutPosition != -1 is here because if you have focus and swipe to delete item it is -1
      name.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && layoutPosition != -1) {
          itemListener?.nameFocusLost(name.text.toString(), items[layoutPosition])
        }
      }
      
      view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewDetachedFromWindow(v: View?) {
          name.clearFocus()
        }
        
        override fun onViewAttachedToWindow(v: View?) {}
      })
    }
    
    fun bindName(name: String) {
      this.name.setText(name)
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
  
  fun swiped(item: Identity<ShoppingItem>)
  
  fun nameFocusLost(name: String, item: Identity<ShoppingItem>)
  
}

fun dpToPx(dp: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics)
