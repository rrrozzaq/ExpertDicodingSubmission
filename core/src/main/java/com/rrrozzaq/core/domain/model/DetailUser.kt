package com.rrrozzaq.core.domain.model

data class DetailUser(
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
