package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.ViewModel

class screenScoreModel(finalscore:Int) :ViewModel(){


   var score = finalscore
    init {
        Log.i("ScoreViewModel", "Final score is $finalscore")
    }





}