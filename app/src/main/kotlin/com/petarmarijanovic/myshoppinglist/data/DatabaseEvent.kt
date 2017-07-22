package com.petarmarijanovic.myshoppinglist.data

/** Created by petar on 22/07/2017. */
enum class Event {
  ADD, UPDATE, REMOVE
}

data class DatabaseEvent<out T>(val event: Event, val item: T)
