package com.jay.typicodeapp.services

import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class is providing a function to check network connection status of the App.
 */
@Singleton
class NetworkHandler @Inject constructor(@ApplicationContext private val context: Context) {

    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.activeNetwork ?: return false
        return true
    }
}
