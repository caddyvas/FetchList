package com.learn.fetchlist.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.learn.fetchlist.R
import com.learn.fetchlist.data.HiringList
import com.learn.fetchlist.repository.NetworkRepository
import com.learn.fetchlist.service.NetworkResponse
import com.learn.fetchlist.ui.adapter.ListViewAdapter
import com.learn.fetchlist.utility.ExtractionUtility
import com.learn.fetchlist.viewmodel.NetworkViewModel
import com.learn.fetchlist.viewmodel.NetworkViewModelFactory

class MainActivity : AppCompatActivity() {
    private var hiringList: ArrayList<HiringList> = ArrayList()
    private var mainHiringList: ArrayList<HiringList> = ArrayList()
    private lateinit var listViewAdapter: ListViewAdapter
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize UI layout elements
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)

        listViewAdapter = ListViewAdapter(this, hiringList)
        val networkRepository = NetworkRepository()
        val networkViewModel = ViewModelProvider(
            this,
            NetworkViewModelFactory(networkRepository)
        )[NetworkViewModel::class.java]

        val observer = Observer<NetworkResponse> { status ->
            when (status) {
                is NetworkResponse.Success -> {
                    println("SUCCESS - List Obtained")
                    // display the list
                    mainHiringList.addAll(removeEmptyStrings(status.data as ArrayList<HiringList>))
                    println("List_Size: ${mainHiringList.size}")
                    handleDropDownListOfIds()
                    // API call response always starts with 1
                    populateListBasedOnId(1)
                }

                is NetworkResponse.Failure -> {
                    // handle messages by response code
                    val responseCode = status.callTypeResponseCode
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

        val listView = findViewById<ListView>(R.id.linear_layout_list_view)
        listView.adapter = listViewAdapter
    }

    /**
     * Handle the dropdown selection of listIds
     */
    private fun handleDropDownListOfIds() {
        //populate the dropdown
        val arrayAdapter = ArrayAdapter(
            this, R.layout.dropdown_item,
            ExtractionUtility.getListOfIdsFromHiringList(mainHiringList).toTypedArray()
        )
        autoCompleteTextView.setAdapter(arrayAdapter)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            parent.getItemAtPosition(position)
            populateListBasedOnId(position + 1)
        }
    }

    /**
     * Update the list based on selected ListID
     */
    private fun populateListBasedOnId(listId: Int) {
        println("SelectedListId:${listId}")
        hiringList.clear()
        hiringList.addAll(mainHiringList.filter { it.listId == listId } as ArrayList<HiringList>)
        println("Reordered List Size: ${hiringList.size}")
        // update the adapter
        listViewAdapter.notifyDataSetChanged()
    }

    /**
     * Method which filters out any items where "name" is blank or null.
     */
    private fun removeEmptyStrings(hiringList: ArrayList<HiringList>): ArrayList<HiringList> {
        hiringList.removeAll { (it.name == "" || it.name == null)}
        return hiringList
    }
}