package com.jay.typicodeapp.features.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.jay.typicodeapp.MainApplication
import com.jay.typicodeapp.services.data.UserData

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : AndroidViewModel(MainApplication.getApplicationContext()) {


    fun getUsers() = liveData {
        try {

            emit(mainRepository.getUsers())

        } catch (e: Exception) {
            emit(listOf<UserData>())

        }
    }
}
