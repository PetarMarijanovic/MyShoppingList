package com.petarmarijanovic.myshoppinglist.screen.lists

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.petarmarijanovic.myshoppinglist.R
import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.repo.ShoppingList
import com.petarmarijanovic.myshoppinglist.screen.items.dpToPx
import timber.log.Timber
import java.util.*

/** Created by petar on 12/07/2017. */
class ListsAdapter : RecyclerView.Adapter<ListsAdapter.ViewHolder>() {
  
  private var items: MutableList<Identity<ShoppingList>> = ArrayList()
  private var listListener: ListListener? = null
  
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
  
  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    attachItemSwipeAnimator(recyclerView)
    attachItemDecorator(recyclerView)
    reuseViewHolder(recyclerView)
  }
  
  private fun attachItemDecorator(recyclerView: RecyclerView) {
    recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
      override fun getItemOffsets(outRect: Rect,
                                  view: View,
                                  parent: RecyclerView,
                                  state: RecyclerView.State) {
        val padding = dpToPx(8f).toInt()
        outRect.top = padding
        outRect.left = padding
        outRect.right = padding
        if (parent.getChildAdapterPosition(view) == parent.adapter.itemCount - 1) outRect.bottom = 8
      }
    })
  }
  
  private fun attachItemSwipeAnimator(recyclerView: RecyclerView) {
    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                                                            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
      override fun onMove(rv: RecyclerView,
                          viewHolder: RecyclerView.ViewHolder,
                          target: RecyclerView.ViewHolder?): Boolean {
        Timber.d("OnMove")
        return false
      }
      
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
        listListener?.swiped(items[viewHolder.adapterPosition])
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
  }
  
  /**
   * Reuse ViewHolder because the adapter creates a new one on every notifyItemChanged without
   * payload, and then cross fades them. I don't use payload because I would have to look for
   * changes manually and then send them as payload.
   */
  private fun reuseViewHolder(recyclerView: RecyclerView) {
    recyclerView.itemAnimator = object : DefaultItemAnimator() {
      override fun canReuseUpdatedViewHolder(
          viewHolder: RecyclerView.ViewHolder, payloads: MutableList<Any>) = true
    }
  }
  
  fun clear() {
    val size = items.size
    items.clear()
    notifyItemRangeRemoved(0, size)
  }
  
  fun addAll(items: List<Identity<ShoppingList>>) {
    this.items.clear()
    this.items.addAll(items)
    notifyDataSetChanged()
  }
  
  fun registerClickListener(listListener: ListListener) {
    this.listListener = listListener
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
      listListener?.click(items[layoutPosition])
    }
  }
}

interface ListListener {
  
  fun swiped(list: Identity<ShoppingList>)
  
  fun click(list: Identity<ShoppingList>)
  
}
