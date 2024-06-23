package com.rrrozzaq.favorite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rrrozzaq.core.domain.model.User
import com.rrrozzaq.core.domain.usecase.UserUseCase
import com.rrrozzaq.core.utils.Async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val userUseCase: UserUseCase) :
    ViewModel() {

    private val _listItems = MutableStateFlow<Async<List<User>>>(Async.Loading)
    val listUsers: StateFlow<Async<List<User>>> = _listItems

    fun getAllFavUser() {
        viewModelScope.launch {
            userUseCase.getFavoriteUser().collect { data ->
                _listItems.value = data
            }
        }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return userUseCase.getThemeSetting().asLiveData()
    }
}