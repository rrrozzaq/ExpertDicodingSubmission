package com.rrrozzaq.core.domain.repository

import com.rrrozzaq.core.domain.model.DetailUser
import com.rrrozzaq.core.domain.model.User
import com.rrrozzaq.core.utils.Async
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUser(username: String): Flow<Async<List<User>>>
    fun getDetailUser(username: String): Flow<Async<DetailUser>>
    fun getFollowers(username: String): Flow<Async<List<User>>>
    fun getFollowing(username: String): Flow<Async<List<User>>>
    fun getFavoriteUser(): Flow<Async<List<User>>>
    fun setFavorite(user: User)
    fun deleteFavorite(id: Int)
    fun saveThemeSetting(username: String): Flow<Int>
    fun getThemeSetting(): Flow<Boolean>
    suspend fun saveThemeSetting(isDarkMode: Boolean)
}