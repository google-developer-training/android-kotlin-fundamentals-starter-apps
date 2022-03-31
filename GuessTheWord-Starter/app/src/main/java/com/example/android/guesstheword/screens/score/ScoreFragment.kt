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

package com.example.android.guesstheword.screens.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.ScoreFragmentBinding
import java.lang.IllegalArgumentException

/**
 * Fragment where the final score is shown, after the game is over
 */
class ScoreFragment : Fragment() {


    private lateinit var scoreModel: screenScoreModel
    private lateinit var viewModelFactory: scoreViewModelFactory
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class.
        val binding: ScoreFragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.score_fragment,
                container,
                false
        )
   viewModelFactory= scoreViewModelFactory(ScoreFragmentArgs.fromBundle(arguments!!).score)
        scoreModel = ViewModelProviders.of(this, viewModelFactory)
                .get(screenScoreModel::class.java)
        binding.scoreText.text = scoreModel.score.toString()
        return binding.root
    }
}
class scoreViewModelFactory(private val finalScore:Int):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

if (modelClass.isAssignableFrom(screenScoreModel::class.java)){
    return screenScoreModel(finalScore)as T
}
throw IllegalArgumentException("Score Not Created")
    }

}