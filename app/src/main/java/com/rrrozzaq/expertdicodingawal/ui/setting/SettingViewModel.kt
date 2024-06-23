package com.rrrozzaq.expertdicodingawal.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.rrrozzaq.core.domain.usecase.UserUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return userUseCase.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            userUseCase.saveThemeSetting(isDarkMode)
        }
    }

}