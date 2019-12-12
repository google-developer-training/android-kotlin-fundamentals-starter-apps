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

package com.example.android.devbyteviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideoDatabase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.DevByteVideo
import com.example.android.devbyteviewer.network.DevByteNetwork
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class VideosRepository(private val videoDatabase: VideoDatabase) {

    val videos: LiveData<List<DevByteVideo>> = Transformations.map(videoDatabase.videDao.getAllVidoe()) {
        it.asDomainModel()
    }

    suspend fun refreshVideso() {
        withContext(Dispatchers.IO) {
            Timber.i("refresh video is called")
            val playList = DevByteNetwork.devbytes.getPlaylistAsync().await()
            videoDatabase.videDao.insertAll(playList.asDatabaseModel())
        }
    }
}
