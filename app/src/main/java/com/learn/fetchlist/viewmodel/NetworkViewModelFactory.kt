package com.learn.fetchlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learn.fetchlist.repository.NetworkRepository
import java.lang.IllegalArgumentException

class NetworkViewModelFactory constructor(private val repository: NetworkRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NetworkViewModel::class.java)) {
            NetworkViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("Error: ViewModel Not Found")
        }
    }
}