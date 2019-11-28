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
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val sleepDatabaseDao: SleepDatabaseDao,
        context: Application
) : AndroidViewModel(context) {

    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var tonight = MutableLiveData<SleepNight?>()
    private var nights = sleepDatabaseDao.getAllSleepNight()

    private var _sleepQualityNavigete = MutableLiveData<SleepNight>()
    val sleepQualityNavigete: LiveData<SleepNight>
        get() = _sleepQualityNavigete

    private var _showSnackbar = MutableLiveData<Boolean>()
    val showSanckBar: LiveData<Boolean>
        get() = _showSnackbar

    init {
        initTonight()
        _sleepQualityNavigete.value = null
    }

    private fun initTonight() {
        uiScope.launch {
            tonight.value = getTonightValueFromDatabase()
        }
    }


    private suspend fun getTonightValueFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = sleepDatabaseDao.getTonight()
            if (night?.startTimeMilli != night?.endTimeMilli) {
                night = null
            }
            night
        }
    }

    fun onStartTracking() {
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightValueFromDatabase()
        }
    }

    fun onStopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)

            _sleepQualityNavigete.value = oldNight
        }

    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            sleepDatabaseDao.update(night)
        }
    }

    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO) {
            sleepDatabaseDao.insert(night)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    var nigthsString: LiveData<Spanned> = Transformations.map(nights) { night ->
        formatNights(night, context.resources)
    }

    fun onClearTracking() {
        uiScope.launch {
            clear()
            tonight.value = null
            _showSnackbar.value = true
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            sleepDatabaseDao.clear()
        }
    }

    fun doneNavigation() {
        _sleepQualityNavigete.value = null
    }

    val startButtonVisible: LiveData<Boolean> = Transformations.map(tonight) {
        it == null
    }

    val stopBUttonVisible: LiveData<Boolean> = Transformations.map(tonight) {
        it != null
    }

    val clearButtonVisible: LiveData<Boolean?> = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    fun doneShowingSnackbar() {
        _showSnackbar.value = false
    }
}