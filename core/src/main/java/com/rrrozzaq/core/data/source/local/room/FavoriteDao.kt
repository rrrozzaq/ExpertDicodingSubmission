package com.rrrozzaq.core.data.source.local.room


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE favorite.id = :id")
    fun delete(id: Int)

    @Query("SELECT * from favorite ORDER BY id ASC")
    fun getAllUser(): Flow<List<FavoriteEntity>>

    @Query("SELECT count(*) FROM favorite WHERE username = :username")
    fun checkFavorite(username: String): Flow<Int>
}