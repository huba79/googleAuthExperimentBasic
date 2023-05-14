package com.example.googleauthexperimentbasic.network

import com.example.googleauthexperimentbasic.UserResponse
import com.example.googleauthexperimentbasic.messaging.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UsersApi {

    @GET("protected/user/{id}")
    suspend fun getUser(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Long
    ): Response<ApiResponse<UserResponse>>
}