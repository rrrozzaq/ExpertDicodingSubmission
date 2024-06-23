package com.rrrozzaq.core.data.source.remote

import com.rrrozzaq.core.data.model.ApiResponse
import com.rrrozzaq.core.data.model.DetailUserResponse
import com.rrrozzaq.core.data.model.FollowersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun getUsers(
        @Query("q") q: String
    ): ApiResponse

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): DetailUserResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username: String
    ): List<FollowersResponse>

    @GET("users/{username}/following")
    suspend fun getFollowing(
        @Path("username") username: String
    ): List<FollowersResponse>
}