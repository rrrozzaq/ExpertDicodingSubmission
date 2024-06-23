package com.rrrozzaq.expertdicodingawal.model

import com.rrrozzaq.core.domain.model.User

data class UserModel(
    val id: Int,
    val username: String,
    val avatarUrl: String,
)

fun User.toModel(): UserModel {
    return UserModel(
        id,
        username,
        avatarUrl
    )
}
