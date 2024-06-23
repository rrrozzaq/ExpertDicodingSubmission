package com.rrrozzaq.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.rrrozzaq.core.data.repository.UserRepositoryImpl
import com.rrrozzaq.core.data.source.local.LocalDataSource
import com.rrrozzaq.core.data.source.remote.RemoteDataSource
import com.rrrozzaq.core.domain.repository.UserRepository
import com.rrrozzaq.core.domain.usecase.UserUseCase
import com.rrrozzaq.core.domain.usecase.UserUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): UserRepository {
        return UserRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideUserUseCase(userRepository: UserRepository): UserUseCase {
        return UserUseCaseImpl(userRepository)
    }

}