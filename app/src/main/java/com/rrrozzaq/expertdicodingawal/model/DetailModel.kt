package com.rrrozzaq.expertdicodingawal.model

import com.rrrozzaq.core.domain.model.DetailUser
import com.rrrozzaq.core.domain.model.User

data class DetailModel(
    val id: Int,
    val fullName: String?,
    val username: String,
    val avatarUrl: String,
    val bio: String?,
    val followingUrl: String,
    val followersUrl: String,
    val following: Int,
    val followers: Int,
)

fun DetailUser.toModel(): DetailModel {
    return DetailModel(
        id,
        fullName,
        username,
        avatarUrl,
        bio,
        followingUrl,
        followersUrl,
        following,
        followers
    )
}

fun DetailModel.toUser(): User {
    return User(
        id,
        username,
        avatarUrl,
    )
}
