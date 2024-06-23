package com.rrrozzaq.expertdicodingawal.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.rrrozzaq.core.domain.model.User
import com.rrrozzaq.core.domain.usecase.UserUseCase
import com.rrrozzaq.core.utils.Async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {

    private val _users = MutableStateFlow<Async<List<User>>>(Async.Loading)
    val users: StateFlow<Async<List<User>>> get() = _users

    init {
        findUser("rozzaq")
    }

    fun findUser(username: String) {
        viewModelScope.launch {
            userUseCase.getAllUser(username).collect {
                _users.value = it
            }
        }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return userUseCase.getThemeSetting().asLiveData()
    }

}