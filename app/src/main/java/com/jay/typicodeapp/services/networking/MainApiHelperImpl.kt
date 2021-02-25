package com.jay.typicodeapp.services.networking

import com.jay.typicodeapp.services.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainApiHelperImpl @Inject constructor(
    private val apiService: MainApiService
) : MainApiHelperInterface {

    override suspend fun getUsers(): List<UserData> = withContext(Dispatchers.IO) {
        apiService.getUsers(mapOf("Content-Type" to "application/json"))
    }
}
