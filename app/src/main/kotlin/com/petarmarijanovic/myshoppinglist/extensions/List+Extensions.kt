package com.petarmarijanovic.myshoppinglist.extensions

/** Created by petar on 03/08/2017. */
inline fun <T> List<T>.safeIndexOfFirst(predicate: (T) -> Boolean, action: (Int) -> Unit): Unit {
  indexOfFirst(predicate).let {
    if (it == -1) return@let
    action(it)
  }
}
