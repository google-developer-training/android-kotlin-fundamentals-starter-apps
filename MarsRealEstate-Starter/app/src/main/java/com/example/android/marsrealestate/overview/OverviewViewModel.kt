/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsAPi
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */

enum class MarsApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<List<MarsProperty>>()

    // The external immutable LiveData for the response String
    val response: LiveData<List<MarsProperty>>
        get() = _response

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */

    private var _status = MutableLiveData<MarsApiStatus>()

    val status: LiveData<MarsApiStatus>
        get() = _status

    private var _navigateToDetailFragment = MutableLiveData<MarsProperty>()

    val navigateToDetailFragment: LiveData<MarsProperty>
        get() = _navigateToDetailFragment

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    fun updateFilter(filter: MarsApiFilter) {
        getMarsRealEstateProperties(filter)
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties(filter: MarsApiFilter) {

        uiScope.launch {
            val deferredResponse = MarsAPi.retrofitService.getPropertiesAsync(filter.value)
            try {
                _status.value = MarsApiStatus.LOADING
                val lisResult = deferredResponse.await()
                if (lisResult.isNotEmpty()) {
                    _response.value = lisResult
                    _status.value = MarsApiStatus.DONE
                }

            } catch (e: Exception) {
                _response.value = null
                _status.value = MarsApiStatus.ERROR

            }

        }

    }

    fun displayProperyDetails(marsProperty: MarsProperty) {
        _navigateToDetailFragment.value = marsProperty
    }

    fun doneNavigatingToDetail() {
        _navigateToDetailFragment.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
