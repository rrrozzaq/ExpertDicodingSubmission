package com.rrrozzaq.core.data.source.local

import com.rrrozzaq.core.data.source.local.datastore.SettingPreferences
import com.rrrozzaq.core.data.source.local.room.FavoriteDao
import com.rrrozzaq.core.domain.model.User
import com.rrrozzaq.core.utils.Async
import com.rrrozzaq.core.utils.DataMapper.toFavoriteEntity
import com.rrrozzaq.core.utils.DataMapper.toUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val settingPreferences: SettingPreferences
) {

    fun insert(user: User) {
        favoriteDao.insert(user.toFavoriteEntity())
    }

    fun delete(id: Int) {
        favoriteDao.delete(id)
    }

    fun getAllUser(): Flow<Async<List<User>>> {
        return flow {
            emit(Async.Loading)
            try {
                favoriteDao.getAllUser().collect { list ->
                    val data = list.map { it.toUser() }
                    emit(Async.Success(data))
                }
            } catch (e: Exception) {
                emit(Async.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun checkFavorite(username: String): Flow<Int> {
        return favoriteDao.checkFavorite(username)
    }

    fun getThemeSetting(): Flow<Boolean> {
        return settingPreferences.getThemeSetting()
    }

    suspend fun saveThemeSetting(isDarkMode: Boolean) {
        settingPreferences.saveThemeSetting(isDarkMode)
    }

}