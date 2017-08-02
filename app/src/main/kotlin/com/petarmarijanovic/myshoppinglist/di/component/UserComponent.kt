package com.petarmarijanovic.myshoppinglist.di.component

import com.petarmarijanovic.myshoppinglist.di.module.RepoModule
import com.petarmarijanovic.myshoppinglist.di.scope.PerUser
import com.petarmarijanovic.myshoppinglist.screen.invitations.InvitationsActivity
import com.petarmarijanovic.myshoppinglist.screen.items.ItemsActivity
import com.petarmarijanovic.myshoppinglist.screen.lists.ListsActivity
import com.petarmarijanovic.myshoppinglist.screen.users.UsersActivity
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

/** Created by petar on 20/07/2017. */
@PerUser
@Subcomponent(modules = arrayOf(RepoModule::class))
interface UserComponent {
  
  @Subcomponent.Builder
  interface Builder {
    @BindsInstance fun email(@Named(RepoModule.NAMED_EMAIL) email: String): Builder
    fun build(): UserComponent
  }
  
  @Named(RepoModule.NAMED_EMAIL)
  fun email(): String
  
  fun inject(target: ItemsActivity)
  
  fun inject(target: UsersActivity)
  
  fun inject(target: InvitationsActivity)
  
  fun inject(target: ListsActivity)
  
}
