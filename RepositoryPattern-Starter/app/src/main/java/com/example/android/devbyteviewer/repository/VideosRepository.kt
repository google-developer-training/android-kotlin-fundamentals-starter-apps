package com.example.android.devbyteviewer.repository

import com.example.android.devbyteviewer.database.VideosDatabase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.DevByteVideo
import com.example.android.devbyteviewer.network.DevByteNetwork
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.flow.*

class VideosRepository(private val database: VideosDatabase) {
    val videos: Flow<List<DevByteVideo>> = database.videoDao.getVideos().mapLatest {
        it.asDomainModel()
    }

    fun refreshVideos(): Flow<List<DevByteVideo>> = flow {
        val playlist = DevByteNetwork.devbytes.getPlaylist()
        database.videoDao.insertAll(playlist.asDatabaseModel())
    }
}