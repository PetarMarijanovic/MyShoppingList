package com.petarmarijanovic.myshoppinglist.data.di

import com.petarmarijanovic.myshoppinglist.application.di.PerUser
import com.petarmarijanovic.myshoppinglist.screen.items.ItemsActivity
import dagger.Subcomponent

/** Created by petar on 20/07/2017. */
@PerUser
@Subcomponent(modules = arrayOf(RepoModule::class))
interface RepoComponent {
  
  fun inject(listsActivity: ItemsActivity)
  
}
