package com.jay.typicodeapp.features.home

import com.jay.typicodeapp.services.networking.MainApiHelperInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private var getData: MainApiHelperInterface
) {

    suspend fun getUsers() = withContext(Dispatchers.IO) {
        getData.getUsers()
    }
}
