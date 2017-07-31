package com.petarmarijanovic.myshoppinglist.di.component

import com.petarmarijanovic.myshoppinglist.data.Identity
import com.petarmarijanovic.myshoppinglist.data.model.User
import com.petarmarijanovic.myshoppinglist.di.module.RepoModule
import com.petarmarijanovic.myshoppinglist.di.scope.PerUser
import com.petarmarijanovic.myshoppinglist.screen.items.ItemsActivity
import com.petarmarijanovic.myshoppinglist.screen.lists.ListsActivity
import com.petarmarijanovic.myshoppinglist.screen.users.UsersActivity
import dagger.BindsInstance
import dagger.Subcomponent

/** Created by petar on 20/07/2017. */
@PerUser
@Subcomponent(modules = arrayOf(RepoModule::class))
interface UserComponent {
  
  @Subcomponent.Builder
  interface Builder {
    @BindsInstance fun user(user: Identity<User>): Builder
    fun build(): UserComponent
  }
  
  fun user(): Identity<User>
  
  fun inject(target: ItemsActivity)
  
  fun inject(target: UsersActivity)
  
  fun inject(target: ListsActivity)
  
}
