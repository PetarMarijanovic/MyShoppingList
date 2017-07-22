package com.petarmarijanovic.myshoppinglist.di.component

import com.petarmarijanovic.myshoppinglist.di.module.RepoModule
import com.petarmarijanovic.myshoppinglist.di.module.UserModule
import com.petarmarijanovic.myshoppinglist.di.scope.PerUser
import com.petarmarijanovic.myshoppinglist.screen.items.ItemsActivity
import com.petarmarijanovic.myshoppinglist.screen.lists.ListsActivity
import dagger.Subcomponent

/** Created by petar on 20/07/2017. */
@PerUser
@Subcomponent(modules = arrayOf(UserModule::class, RepoModule::class))
interface UserComponent {
  
  fun inject(target: ItemsActivity)
  
  fun inject(target: ListsActivity)
  
}
