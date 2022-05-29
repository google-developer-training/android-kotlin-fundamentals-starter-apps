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

package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [SleepNight::class], exportSchema = false)
abstract class SleepDatabase: RoomDatabase() {

    abstract val sleepDatabaseDao: SleepDatabaseDao

    companion object {
        // The value of a volatile variable will never be cached,
        // and all writes and reads will be done to and from the main memory.
        // This helps make sure the value of INSTANCE is always up-to-date and the same to all execution threads.
        // It means that changes made by one thread to INSTANCE are visible to all other threads immediately,
        // and you don't get a situation where, say, two threads each update the same entity in a cache, which would create a problem.
        @Volatile
        private var INSTANCE: SleepDatabase? = null

        fun getInstance(context: Context): SleepDatabase {
            // means that only one thread of execution at a time can enter this block of code,
            // which makes sure the database only gets initialized once
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepDatabase::class.java,
                        "sleep_history_database"
                    ).build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}
