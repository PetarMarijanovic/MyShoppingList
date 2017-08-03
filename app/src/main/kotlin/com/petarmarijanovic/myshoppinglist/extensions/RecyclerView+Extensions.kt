package com.petarmarijanovic.myshoppinglist.extensions

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.*
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.*
import android.view.View

/** Created by petar on 03/08/2017. */
fun RecyclerView.attachLeftRightSwipeAnimator(swipeListener: ((position: Int) -> Unit)) =
    ItemTouchHelper(object : SimpleCallback(0, LEFT or RIGHT) {
      override fun onMove(rv: RecyclerView, vh: ViewHolder, target: ViewHolder) = false
      
      override fun onSwiped(vh: ViewHolder, swipeDir: Int) {
        swipeListener(vh.adapterPosition)
      }
      
      override fun onChildDraw(c: Canvas,
                               rv: RecyclerView,
                               vh: ViewHolder,
                               dX: Float,
                               dY: Float,
                               actionState: Int,
                               isCurrentlyActive: Boolean) {
        if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE) {
          super.onChildDraw(c, rv, vh, dX, dY, actionState, isCurrentlyActive)
        }
        
        val alpha = 1.0f - (Math.abs(dX) / vh.itemView.width) * 2
        vh.itemView.alpha = Math.max(0.2f, alpha)
        vh.itemView.translationX = dX
      }
    }).attachToRecyclerView(this)

fun RecyclerView.addPaddingForItems(paddingInDp: Int) {
  addItemDecoration(object : ItemDecoration() {
    override fun getItemOffsets(rect: Rect, v: View, rv: RecyclerView, state: State) {
      rect.apply {
        top = paddingInDp
        left = paddingInDp
        right = paddingInDp
        if (getChildAdapterPosition(v) == adapter.itemCount - 1) bottom = paddingInDp
      }
    }
  })
}

fun RecyclerView.reuseViewHolder() {
  itemAnimator = object : DefaultItemAnimator() {
    override fun canReuseUpdatedViewHolder(vh: ViewHolder, payloads: MutableList<Any>) = true
  }
}
