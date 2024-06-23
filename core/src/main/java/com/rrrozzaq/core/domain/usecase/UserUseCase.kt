package com.rrrozzaq.core.domain.usecase

import com.rrrozzaq.core.domain.model.DetailUser
import com.rrrozzaq.core.domain.model.User
import com.rrrozzaq.core.domain.repository.UserRepository
import com.rrrozzaq.core.utils.Async
import kotlinx.coroutines.flow.Flow

class UserUseCaseImpl(private val userRepository: UserRepository) : UserUseCase {
    override fun getAllUser(username: String): Flow<Async<List<User>>> {
        return userRepository.getAllUser(username)
    }

    override fun getDetailUser(username: String): Flow<Async<DetailUser>> {
        return userRepository.getDetailUser(username)
    }

    override fun getFollowers(username: String): Flow<Async<List<User>>> {
        return userRepository.getFollowers(username)
    }

    override fun getFollowing(username: String): Flow<Async<List<User>>> {
        return userRepository.getFollowing(username)
    }

    override fun getFavoriteUser(): Flow<Async<List<User>>> {
        return userRepository.getFavoriteUser()
    }

    override fun setFavorite(user: User) {
        userRepository.setFavorite(user)
    }

    override fun deleteFavorite(id: Int) {
        userRepository.deleteFavorite(id)
    }

    override fun checkFavorite(username: String): Flow<Int> {
        return userRepository.saveThemeSetting(username)
    }

    override fun getThemeSetting(): Flow<Boolean> {
        return userRepository.getThemeSetting()
    }

    override suspend fun saveThemeSetting(isDarkMode: Boolean) {
        userRepository.saveThemeSetting(isDarkMode)
    }

}

interface UserUseCase {
    fun getAllUser(username: String): Flow<Async<List<User>>>
    fun getDetailUser(username: String): Flow<Async<DetailUser>>
    fun getFollowers(username: String): Flow<Async<List<User>>>
    fun getFollowing(username: String): Flow<Async<List<User>>>
    fun getFavoriteUser(): Flow<Async<List<User>>>
    fun setFavorite(user: User)
    fun deleteFavorite(id: Int)
    fun checkFavorite(username: String): Flow<Int>
    fun getThemeSetting(): Flow<Boolean>
    suspend fun saveThemeSetting(isDarkMode: Boolean)
}