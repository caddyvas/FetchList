package com.learn.fetchlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.learn.fetchlist.repository.NetworkRepository
import com.learn.fetchlist.service.NetworkResponse

class NetworkViewModel constructor(networkRepository: NetworkRepository) : ViewModel() {

    private val networkRepository : NetworkRepository = networkRepository
    var networkResponse: LiveData<NetworkResponse> = this.networkRepository.networkObserver

    fun obtainFetchList() {
        networkRepository.getFetchList()
    }
}