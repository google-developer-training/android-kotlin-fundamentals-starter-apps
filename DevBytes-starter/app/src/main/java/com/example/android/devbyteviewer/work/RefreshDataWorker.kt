package com.example.android.devbyteviewer.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.repository.VideosRepository
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWorker(appContext: Context,
                        params: WorkerParameters) :
        CoroutineWorker(appContext, params) {

    companion object {
        const val WOEK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val repository = VideosRepository(getDatabase(applicationContext))

        try {
            repository.refreshVideso()
            Timber.i("refresh method is called")
        } catch (e: HttpException) {
            Result.retry()
        }
        return Result.success()
    }

}