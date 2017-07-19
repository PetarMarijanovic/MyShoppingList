package com.petarmarijanovic.myshoppinglist.screen.items

import android.graphics.Canvas
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.ShoppingItem
import timber.log.Timber

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
    ItemTouchHelper(object : SimpleCallback(0, LEFT or RIGHT) {
      override fun onMove(rv: RecyclerView,
                          viewHolder: RecyclerView.ViewHolder,
                          target: RecyclerView.ViewHolder?): Boolean {
        Timber.d("OnMove")
        return false
      }
      
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
        itemListener?.swiped(items[viewHolder.adapterPosition])
      }
      
      override fun onChildDraw(c: Canvas,
                               rv: RecyclerView,
                               viewHolder: RecyclerView.ViewHolder,
                               dX: Float,
                               dY: Float,
                               actionState: Int,
                               isCurrentlyActive: Boolean) {
        if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE) {
          super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
        
        val alpha = 1.0f - (Math.abs(dX) / viewHolder.itemView.width) * 2
        viewHolder.itemView.alpha = if (alpha < 0.2f) 0.2f else alpha
        viewHolder.itemView.translationX = dX
      }
    }).attachToRecyclerView(recyclerView)
    
    recyclerView.itemAnimator = object : DefaultItemAnimator() {
      override fun canReuseUpdatedViewHolder(
          viewHolder: RecyclerView.ViewHolder, payloads: MutableList<Any>) = true
    }
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
        itemListener?.checked(isChecked, items[layoutPosition])
      }
      minus.setOnClickListener { itemListener?.minus(items[layoutPosition]) }
      plus.setOnClickListener { itemListener?.plus(items[layoutPosition]) }
      
      name.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) itemListener?.nameFocusLost(name.text.toString(), items[layoutPosition])
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
