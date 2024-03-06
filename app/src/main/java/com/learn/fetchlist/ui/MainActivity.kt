package com.learn.fetchlist.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.learn.fetchlist.R
import com.learn.fetchlist.data.HiringList
import com.learn.fetchlist.repository.NetworkRepository
import com.learn.fetchlist.service.NetworkResponse
import com.learn.fetchlist.viewmodel.NetworkViewModel
import com.learn.fetchlist.viewmodel.NetworkViewModelFactory
import io.realm.Realm
import io.realm.RealmConfiguration

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.intro_text)

        setupRealm()
        val networkRepository = NetworkRepository()
        val networkViewModel = ViewModelProvider(
            this,
            NetworkViewModelFactory(networkRepository)
        )[NetworkViewModel::class.java]

        val observer = Observer<NetworkResponse> { status ->
            when (status) {
                is NetworkResponse.Success -> {
                    println("SUCCESS - List Obtained")
                    textView.text = "LIST DOWNLOAD SUCCESS"
                    // display the list //TODO - should be removed once UI is implemented
                    val hiringList: List<HiringList> = status.data as List<HiringList>
                    println("cook: ${hiringList.size}")
                    for (item in hiringList) {
                        println("${item.id} --- ${item.listId} --- ${item.name}")
                    }
                }

                is NetworkResponse.Failure -> {
                    // handle messages by response code
                    val responseCode = status.callTypeResponseCode
                    textView.text = "LIST DOWNLOAD FAILED"
                    when (responseCode) {
                        in 500..503 -> {
                            // display a generic message
                            // TODO
                            return@Observer
                        }

                        504 -> {
                            // TODO
                            return@Observer
                        }

                        403 -> {
                            // TODO
                            return@Observer
                        }
                    }
                }
            }
        }
        networkViewModel.networkResponse.observe(this, observer)
        networkViewModel.obtainFetchList()
    }

    private fun setupRealm() {
        Realm.init(this)
        val realmBuilder = RealmConfiguration.Builder().name("hiringDetails.realm")
        val config = realmBuilder.build()
        Realm.setDefaultConfiguration(config)
    }
}