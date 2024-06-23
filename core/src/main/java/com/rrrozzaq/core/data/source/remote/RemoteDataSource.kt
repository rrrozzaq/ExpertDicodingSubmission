package com.rrrozzaq.core.data.source.remote

import android.util.Log
import com.rrrozzaq.core.domain.model.DetailUser
import com.rrrozzaq.core.domain.model.User
import com.rrrozzaq.core.utils.Async
import com.rrrozzaq.core.utils.DataMapper.toDetailUser
import com.rrrozzaq.core.utils.DataMapper.toUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    fun getAllUsers(username: String): Flow<Async<List<User>>> {
        return flow {
            emit(Async.Loading)
            try {
                Log.d("RemoteDataSource", "getAllUsers: ${apiService.getUsers(username)}")
                val data = apiService.getUsers(username).items.map { it.toUser() }
                emit(Async.Success(data))
            } catch (e: Exception) {
                emit(Async.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getDetailUser(username: String): Flow<Async<DetailUser>> {
        return flow {
            emit(Async.Loading)
            try {
                val data = apiService.getDetailUser(username).toDetailUser()
                emit(Async.Success(data))
            } catch (e: Exception) {
                emit(Async.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getFollowers(username: String): Flow<Async<List<User>>> {
        return flow {
            emit(Async.Loading)
            try {
                val data = apiService.getFollowers(username).map { it.toUser() }
                emit(Async.Success(data))
            } catch (e: Exception) {
                emit(Async.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getFollowing(username: String): Flow<Async<List<User>>> {
        return flow {
            emit(Async.Loading)
            try {
                val data = apiService.getFollowing(username).map { it.toUser() }
                emit(Async.Success(data))
            } catch (e: Exception) {
                emit(Async.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

}