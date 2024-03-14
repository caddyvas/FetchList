package com.learn.fetchlist.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.learn.fetchlist.data.HiringList
import com.learn.fetchlist.service.INetworkAPIService
import com.learn.fetchlist.service.NetworkResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.Response
import java.lang.Exception

class NetworkRepository : INetworkRepository {

    var networkObserver: LiveData<NetworkResponse>
    private val networkResponse = MutableLiveData<NetworkResponse>()

    init {
        // apply transformation to observe change in network response
        networkObserver = Transformations.map(networkResponse) { getNetworkResponse() }
    }

    private fun getNetworkResponse(): NetworkResponse? {
        return networkResponse.value
    }

    override fun getFetchList() {
        var hiringListDetails : List<HiringList> = ArrayList()
        var iNetworkAPIService: INetworkAPIService =
            INetworkAPIService.create("https://fetch-hiring.s3.amazonaws.com/")
        val fetchListProcess = CoroutineScope(IO).launch {
            launch {
                val response: Response<List<HiringList>> =
                    iNetworkAPIService.getFetchHiringList()
                if (response.isSuccessful) {
                    response.body()?.let { listDetails ->
                        if (listDetails.isNotEmpty()) {
                            hiringListDetails = listDetails
                            println("List Downloaded Successfully")
                        } else {
                            println("List Download Failed")
                        }
                    }
                } else {
                    println("Failed: ${response.code()}")
                }
            }
        }

        fetchListProcess.invokeOnCompletion { throwable ->
            if (throwable != null) {
                networkResponse.postValue(NetworkResponse.Failure(Exception("Failed")))
            } else {
                networkResponse.postValue(NetworkResponse.Success(hiringListDetails))
            }
        }
    }
}