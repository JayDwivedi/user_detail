package com.jay.typicodeapp.services.networking

import com.jay.typicodeapp.services.data.UserData
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface MainApiService {

    @GET("/users")
    suspend fun getUsers(
        @HeaderMap headers: Map<String, String>
    ): List<UserData>

}
