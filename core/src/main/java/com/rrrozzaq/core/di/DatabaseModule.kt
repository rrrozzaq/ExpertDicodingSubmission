package com.rrrozzaq.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.rrrozzaq.core.data.source.local.room.FavoriteDao
import com.rrrozzaq.core.data.source.local.room.FavoriteDatabase
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FavoriteDatabase {
        val passPhrase: ByteArray = SQLiteDatabase.getBytes("favDBPass".toCharArray())
        val factory = SupportFactory(passPhrase)
        return Room.databaseBuilder(
            context.applicationContext,
            FavoriteDatabase::class.java,
            "Favorite.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: FavoriteDatabase): FavoriteDao = database.favoriteDao()

}