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

    val loaderState: LiveData<Int> by lazy { _loaderState }

    /**
     * Backing field for [loaderState]
     */
    private val _loaderState: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    fun getUsers() = liveData {
        try {
            _loaderState.postValue(1)
            emit(mainRepository.getUsers())
            _loaderState.postValue(0)
        } catch (e: Exception) {
            emit(listOf<UserData>())
            _loaderState.postValue(0)
        }
    }
}
