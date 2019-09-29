package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


/**
 * Created by Chris Athanas on 2019-09-27.
 */
class ScoreViewModel(finalScore: Int) : ViewModel() {
    // the final score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    init{
        Log.i("ScoreViewModel", "Final score is $finalScore")
        _score.value = finalScore
    }
}