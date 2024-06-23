package com.rrrozzaq.expertdicodingawal.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.rrrozzaq.core.domain.model.DetailUser
import com.rrrozzaq.core.domain.model.User
import com.rrrozzaq.core.domain.usecase.UserUseCase
import com.rrrozzaq.core.utils.Async
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {

    private val _detailUser = MutableStateFlow<Async<DetailUser>>(Async.Loading)
    private val _listFollowers = MutableStateFlow<Async<List<User>>>(Async.Loading)
    private val _listFollowing = MutableStateFlow<Async<List<User>>>(Async.Loading)
    private val _isFavorite = MutableStateFlow(0)

    val detailUser: StateFlow<Async<DetailUser>> = _detailUser
    val listFollowers: StateFlow<Async<List<User>>> = _listFollowers
    val listFollowing: StateFlow<Async<List<User>>> = _listFollowing
    val isFavorite: StateFlow<Int> = _isFavorite

    fun showUser(username: String) {
        viewModelScope.launch {
            userUseCase.getDetailUser(username).collect {
                _detailUser.value = it
            }
        }
    }

    fun showFollowers(username: String) {
        viewModelScope.launch {
            userUseCase.getFollowers(username).collect {
                _listFollowers.value = it
            }
        }
    }

    fun showFollowing(username: String) {
        viewModelScope.launch {
            userUseCase.getFollowing(username).collect {
                _listFollowing.value = it
            }
        }
    }

    fun favUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("DetailViewModel", "Adding user to favorites: $user")
            userUseCase.setFavorite(user)
        }
    }

    fun unFavUser(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("DetailViewModel", "Removing user from favorites with ID: $id")
            userUseCase.deleteFavorite(id)
        }
    }

    fun checkUser(username: String) {
        viewModelScope.launch {
            userUseCase.checkFavorite(username).collect {
                _isFavorite.value = it
            }
        }
    }
}