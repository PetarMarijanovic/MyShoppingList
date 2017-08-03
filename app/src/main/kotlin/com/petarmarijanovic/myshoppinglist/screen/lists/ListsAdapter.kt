package com.petarmarijanovic.myshoppinglist.screen.lists

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingItem
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingList
import com.petarmarijanovic.myshoppinglist.extensions.addPaddingForItems
import com.petarmarijanovic.myshoppinglist.extensions.attachLeftRightSwipeAnimator
import com.petarmarijanovic.myshoppinglist.extensions.safeIndexOfFirst
import com.petarmarijanovic.myshoppinglist.screen.items.dpToPx
import kotlinx.android.synthetic.main.item_shopping_item_small.view.*
import java.util.*

/** Created by petar on 12/07/2017. */
class ListsAdapter : RecyclerView.Adapter<ListsAdapter.ViewHolder>() {
  
  private val lists: MutableList<Identity<ShoppingList>> = ArrayList()
  private var listener: ListsListener? = null
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
      ViewHolder(
          LayoutInflater.from(parent.context).inflate(R.layout.list_shopping_list, parent, false))
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val list = lists[position].value
    holder.bindName(list.name)
    holder.bindItems(list.items)
  }
  
  override fun getItemCount() = lists.size
  
  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    recyclerView.apply {
      attachLeftRightSwipeAnimator { listener?.onSwipe(lists[it]) }
      addPaddingForItems(dpToPx(8f).toInt())
    }
  }
  
  fun clear() {
    val size = lists.size
    lists.clear()
    notifyItemRangeRemoved(0, size)
  }
  
  fun add(list: Identity<ShoppingList>) {
    lists.add(list)
    notifyItemInserted(lists.size - 1)
  }
  
  fun update(list: Identity<ShoppingList>) =
      lists.safeIndexOfFirst({ it.id == list.id }) {
        lists[it] = list
        notifyItemChanged(it)
      }
  
  fun remove(id: String) =
      lists.safeIndexOfFirst({ it.id == id }) {
        lists.removeAt(it)
        notifyItemRemoved(it)
      }
  
  fun registerClickListener(listener: ListsListener) {
    this.listener = listener
  }
  
  inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    
    private val name = view.findViewById(R.id.name) as TextView
    private val items_container = view.findViewById(R.id.items_container) as LinearLayout
    
    init {
      view.setOnClickListener { listener?.onClick(lists[layoutPosition]) }
    }
    
    fun bindName(value: String) {
      name.text = value
    }
    
    fun bindItems(items: List<Identity<ShoppingItem>>) {
      items_container.removeAllViews()
      items.map { it.value }
          .sortedBy { it.checked }
          .take(5)
          .map { createItemView(it) }
          .forEach { items_container.addView(it) }
    }
    
    private fun createItemView(item: ShoppingItem) =
        LayoutInflater.from(view.context).inflate(R.layout.item_shopping_item_small, null).apply {
          checkbox.isChecked = item.checked
          item_name.text = item.name
          quantity.text = item.quantity.toString()
        }
  }
}

interface ListsListener {
  
  fun onSwipe(list: Identity<ShoppingList>)
  
  fun onClick(list: Identity<ShoppingList>)
  
}
