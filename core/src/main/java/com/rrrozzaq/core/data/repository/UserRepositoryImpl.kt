package com.rrrozzaq.core.data.repository

import com.rrrozzaq.core.data.source.local.LocalDataSource
import com.rrrozzaq.core.data.source.remote.RemoteDataSource
import com.rrrozzaq.core.domain.model.DetailUser
import com.rrrozzaq.core.domain.model.User
import com.rrrozzaq.core.domain.repository.UserRepository
import com.rrrozzaq.core.utils.Async
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : UserRepository {

    override fun getAllUser(username: String): Flow<Async<List<User>>> {
        return remoteDataSource.getAllUsers(username)
    }

    override fun getDetailUser(username: String): Flow<Async<DetailUser>> {
        return remoteDataSource.getDetailUser(username)
    }

    override fun getFollowers(username: String): Flow<Async<List<User>>> {
        return remoteDataSource.getFollowers(username)
    }

    override fun getFollowing(username: String): Flow<Async<List<User>>> {
        return remoteDataSource.getFollowing(username)
    }

    override fun getFavoriteUser(): Flow<Async<List<User>>> {
        return localDataSource.getAllUser()
    }

    override fun setFavorite(user: User) {
        return localDataSource.insert(user)
    }

    override fun deleteFavorite(id: Int) {
        return localDataSource.delete(id)
    }

    override fun saveThemeSetting(username: String): Flow<Int> {
        return localDataSource.checkFavorite(username)
    }

    override fun getThemeSetting(): Flow<Boolean> {
        return localDataSource.getThemeSetting()
    }

    override suspend fun saveThemeSetting(isDarkMode: Boolean) {
        localDataSource.saveThemeSetting(isDarkMode)
    }

}