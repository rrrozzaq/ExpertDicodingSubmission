package com.rrrozzaq.favorite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rrrozzaq.core.domain.usecase.UserUseCase
import com.rrrozzaq.expertdicodingawal.di.DomainUseCase
import javax.inject.Inject

class ViewModelFactory @Inject constructor(@DomainUseCase private val userUseCase: UserUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(userUseCase) as T
            }

            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}