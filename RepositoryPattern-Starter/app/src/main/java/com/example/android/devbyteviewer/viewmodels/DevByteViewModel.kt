/*
 * Copyright (C) 2019 Google Inc.
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

package com.example.android.devbyteviewer.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.domain.DevByteVideo
import com.example.android.devbyteviewer.repository.VideosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * DevByteViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class DevByteViewModel(application: Application) : AndroidViewModel(application) {

    /**refreshDataFromRepository
     * The data source this ViewModel will fetch results from.
     */
    private val videosRepository = VideosRepository(getDatabase(application))

    /**
     * A playlist of videos displayed on the screen.
     */
    // TODO: Replace the MutableLiveData and backing property below to a reference to the 'videos'
    // TODO: from the VideosRepository
    val playlist = videosRepository.videos.asLiveData()
    /**
     * A playlist of videos that can be shown on the screen. This is private to avoid exposing a
     * way to set this value to observers.
     */

    /**
     * A playlist of videos that can be shown on the screen. Views should use this to get access
     * to the data.
     */

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag to display the error message. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        // TODO: Replace with a call to the refreshDataFromRepository9) method
        refreshDataFromRepository()
    }

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    // TODO: Replace with the refreshDataFromRepository() method
    private fun refreshDataFromRepository() = viewModelScope.launch {
        try {
            videosRepository.refreshVideos()
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
        } catch (networkError: IOException) {
            if (playlist.value.isNullOrEmpty()) {
                _eventNetworkError.value = true
            }
        }
    }

    /**
     * Resets the network error flag.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevByteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
