package com.petarmarijanovic.myshoppinglist.data.model

import com.petarmarijanovic.myshoppinglist.data.Identity
import java.util.*

/** Created by petar on 12/07/2017. */
data class ShoppingList(val name: String = "",
                        val users: List<Identity<User>> = Collections.emptyList(),
                        val items: List<Identity<ShoppingItem>> = Collections.emptyList())
