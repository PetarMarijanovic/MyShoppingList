package com.petarmarijanovic.myshoppinglist.di.component

import com.petarmarijanovic.myshoppinglist.di.module.RepoModule
import com.petarmarijanovic.myshoppinglist.di.scope.PerUser
import com.petarmarijanovic.myshoppinglist.screen.items.ItemsActivity
import com.petarmarijanovic.myshoppinglist.screen.lists.ListsActivity
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

/** Created by petar on 20/07/2017. */
@PerUser
@Subcomponent(modules = arrayOf(RepoModule::class))
interface UserComponent {
  
  companion object {
    const val NAMED_UID = "uid"
  }
  
  @Subcomponent.Builder
  interface Builder {
    @BindsInstance fun uid(@Named(NAMED_UID) uid: String): Builder
    fun build(): UserComponent
  }
  
  @Named(NAMED_UID) fun uid(): String
  
  fun inject(target: ItemsActivity)
  
  fun inject(target: ListsActivity)
  
}
