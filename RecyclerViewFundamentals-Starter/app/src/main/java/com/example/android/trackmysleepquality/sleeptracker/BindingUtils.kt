package com.example.android.trackmysleepquality.sleeptracker

import android.text.format.DateUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight


@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(night: SleepNight) {
    text = DateUtils.formatDateRange(context, night.startTimeMilli, night.endTimeMilli, DateUtils.FORMAT_SHOW_TIME)
}

@BindingAdapter("sleepQuality")
fun TextView.setSleepQuality(night: SleepNight) {
    text = night.sleepQuality.toString()
}

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(night: SleepNight) {
    setImageResource(when (night.sleepQuality) {
        0 -> R.drawable.ic_sleep_0
        1 -> R.drawable.ic_sleep_1
        2 -> R.drawable.ic_sleep_2
        3 -> R.drawable.ic_sleep_3
        4 -> R.drawable.ic_sleep_4
        5 -> R.drawable.ic_sleep_5
        else -> R.drawable.ic_sleep_active
    })
}

