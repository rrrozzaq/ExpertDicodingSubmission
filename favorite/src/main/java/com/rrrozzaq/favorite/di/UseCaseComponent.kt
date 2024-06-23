package com.rrrozzaq.favorite.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import com.rrrozzaq.favorite.ui.FavoriteActivity
import com.rrrozzaq.expertdicodingawal.di.UseCaseDependency

@Component(dependencies = [UseCaseDependency::class])
interface UseCaseComponent {

    fun inject(activity: FavoriteActivity)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(useCaseDependency: UseCaseDependency): Builder
        fun build(): UseCaseComponent
    }
}