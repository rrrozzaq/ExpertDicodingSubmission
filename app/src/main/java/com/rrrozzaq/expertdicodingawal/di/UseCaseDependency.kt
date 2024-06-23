package com.rrrozzaq.expertdicodingawal.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.rrrozzaq.core.domain.usecase.UserUseCase

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UseCaseDependency {

    @DomainUseCase
    fun provideUseCase(): UserUseCase

}

annotation class DomainUseCase
