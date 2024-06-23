package com.rrrozzaq.core.domain.usecase

import com.rrrozzaq.core.domain.model.DetailUser
import com.rrrozzaq.core.domain.model.User
import com.rrrozzaq.core.domain.repository.UserRepository
import com.rrrozzaq.core.utils.Async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

class UserUseCaseTest {

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var userUseCase: UserUseCase

    private val tUser = User(
        id = 1,
        username = "Anonymous",
        avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
    )
    private val tDetailUser = DetailUser(
        id = tUser.id,
        fullName = null,
        username = tUser.username,
        avatarUrl = tUser.avatarUrl,
        bio = null,
        followingUrl = "",
        followersUrl = "",
        following = 0,
        followers = 0,
    )

    private fun <T> T.toFlow(): Flow<Async<T>> = flow { emit(Async.Success(this@toFlow)) }

    @Before
    fun setUp() {
        userRepository = mock {
            onBlocking { getAllUser(tUser.username) }.thenReturn(listOf(tUser).toFlow())
            onBlocking { getDetailUser(tUser.username) }.thenReturn(tDetailUser.toFlow())
            onBlocking { getFollowers(tUser.username) }.thenReturn(listOf(tUser).toFlow())
            onBlocking { getFollowing(tUser.username) }.thenReturn(listOf(tUser).toFlow())
            onBlocking { getFavoriteUser() }.thenReturn(listOf(tUser).toFlow())
            onBlocking { getFavoriteUser() }.thenReturn(listOf(tUser).toFlow())
            onBlocking { saveThemeSetting(tUser.username) }.thenReturn(flow { emit(1) })
        }
        userUseCase = UserUseCaseImpl(userRepository)
    }

    @Test
    fun getAllUser() = runBlocking {
        userUseCase.getAllUser(tUser.username).collect {
            when (it) {
                is Async.Success -> {
                    assertEquals(listOf(tUser), it.data)
                }

                else -> {}
            }
        }
        verify(userRepository).getAllUser(tUser.username)
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun getDetailUser() = runBlocking {
        userUseCase.getDetailUser(tUser.username).collect {
            when (it) {
                is Async.Success -> {
                    assertEquals(tDetailUser, it.data)
                }

                else -> {}
            }
        }
        verify(userRepository).getDetailUser(tUser.username)
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun getFollowers() = runBlocking {
        userUseCase.getFollowers(tUser.username).collect {
            when (it) {
                is Async.Success -> {
                    assertEquals(listOf(tUser), it.data)
                }

                else -> {}
            }
        }
        verify(userRepository).getFollowers(tUser.username)
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun getFollowing() = runBlocking {
        userUseCase.getFollowing(tUser.username).collect {
            when (it) {
                is Async.Success -> {
                    assertEquals(listOf(tUser), it.data)
                }

                else -> {}
            }
        }
        verify(userRepository).getFollowing(tUser.username)
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun getFavoriteUser() = runBlocking {
        userUseCase.getFavoriteUser().collect {
            when (it) {
                is Async.Success -> {
                    assertEquals(listOf(tUser), it.data)
                }

                else -> {}
            }
        }
        verify(userRepository).getFavoriteUser()
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun setFavorite() {
        userUseCase.setFavorite(tUser)
        verify(userRepository).setFavorite(tUser)
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun deleteFavorite() {
        userUseCase.deleteFavorite(tUser.id)
        verify(userRepository).deleteFavorite(tUser.id)
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun checkFavorite() = runBlocking {
        userUseCase.checkFavorite(tUser.username).collect {
            assertEquals(1, it)
        }
        verify(userRepository).saveThemeSetting(tUser.username)
        verifyNoMoreInteractions(userRepository)
    }
}