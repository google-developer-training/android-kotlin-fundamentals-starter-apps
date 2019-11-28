/*
 *  Copyright 2019, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.marsrealestate.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.marsrealestate.R

import com.example.android.marsrealestate.network.MarsProperty

/**
 * The [ViewModel] that is associated with the [DetailFragment].
 */
class DetailViewModel(
        val marsProperty: MarsProperty,
        app: Application
) : AndroidViewModel(app) {

    private var _selectedMarsProperty = MutableLiveData<MarsProperty>()

    val selectedMarsProperty: LiveData<MarsProperty>
        get() = _selectedMarsProperty

    init {
        _selectedMarsProperty.value = marsProperty
    }

    val displayPropertyType = Transformations.map(selectedMarsProperty) {
        app.applicationContext.getString(R.string.display_type,
                app.applicationContext.getString(
                        when (it.isRental) {
                            true -> R.string.type_rent
                            false -> R.string.type_sale
                        }))
    }

    val displayPropertyPrice = Transformations.map(selectedMarsProperty) {
        app.applicationContext.getString(
                when (it.isRental) {
                    true -> R.string.display_price_monthly_rental
                    false -> R.string.display_price
                }, it.price)
    }
}