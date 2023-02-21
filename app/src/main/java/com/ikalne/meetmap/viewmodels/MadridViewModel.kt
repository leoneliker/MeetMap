package com.ikalne.meetmap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ikalne.meetmap.api.models.LocatorView
import com.ikalne.meetmap.api.services.MadridApiRequestManager
import kotlinx.coroutines.*

class MadridViewModel : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val mutableLocators = MutableLiveData<List<LocatorView>>()
    val locators : LiveData<List<LocatorView>>
        get() = mutableLocators



    fun fetchData(){
        val manager = MadridApiRequestManager()
        uiScope.launch {
            val newList = mutableListOf<LocatorView>()
            val madridResponse = withContext(Dispatchers.IO){
                manager.getAll()
            }

            madridResponse.graph.forEach {
                newList.add(LocatorView().fromGraph(it))
            }

            mutableLocators.value = newList
        }
    }
}