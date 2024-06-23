package com.rrrozzaq.core.utils

import com.rrrozzaq.core.data.model.DetailUserResponse
import com.rrrozzaq.core.data.model.FollowersResponse
import com.rrrozzaq.core.data.model.UserResponse
import com.rrrozzaq.core.data.source.local.room.FavoriteEntity
import com.rrrozzaq.core.domain.model.DetailUser
import com.rrrozzaq.core.domain.model.User

object DataMapper {

    fun UserResponse.toUser(): User {
        return User(
            this.id,
            this.login,
            this.avatarUrl,
        )
    }

    fun DetailUserResponse.toDetailUser(): DetailUser {
        return DetailUser(
            this.id,
            this.login,
            this.login,
            this.avatarUrl,
            this.bio,
            this.followingUrl,
            this.followersUrl,
            this.following,
            this.followers
        )
    }

    fun FollowersResponse.toUser(): User {
        return User(
            this.id,
            this.login,
            this.avatarUrl,
        )
    }

    fun FavoriteEntity.toUser(): User {
        return User(
            this.id,
            this.username,
            this.avatarUrl,
        )
    }

    fun User.toFavoriteEntity(): FavoriteEntity {
        return FavoriteEntity(
            this.id,
            this.username,
            this.avatarUrl,
        )
    }

}