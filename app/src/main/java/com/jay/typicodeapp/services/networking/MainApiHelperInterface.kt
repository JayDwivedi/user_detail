package com.jay.typicodeapp.services.networking

import com.jay.typicodeapp.services.data.UserData

interface MainApiHelperInterface {

    suspend fun getUsers(): List<UserData>
}
