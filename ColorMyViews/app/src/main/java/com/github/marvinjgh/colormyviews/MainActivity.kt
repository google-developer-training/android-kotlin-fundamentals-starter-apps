package com.github.marvinjgh.colormyviews

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListeners()
    }

    private fun makeColored(view: View) {
        when (view.id) {

            // Boxes using Color class colors for the background
            R.id.textView1 -> view.setBackgroundColor(Color.DKGRAY)
            R.id.textView2 -> view.setBackgroundColor(Color.GRAY)
            R.id.textView3 -> view.setBackgroundColor(Color.BLUE)
            R.id.textView4 -> view.setBackgroundColor(Color.MAGENTA)
            R.id.textView5 -> view.setBackgroundColor(Color.BLUE)
            else -> view.setBackgroundColor(Color.LTGRAY)
        }
    }

    private fun setListeners() {

        val boxOneText = findViewById<TextView>(R.id.textView1)
        val boxTwoText = findViewById<TextView>(R.id.textView2)
        val boxThreeText = findViewById<TextView>(R.id.textView3)
        val boxFourText = findViewById<TextView>(R.id.textView4)
        val boxFiveText = findViewById<TextView>(R.id.textView5)

        val rootConstraintLayout = findViewById<View>(R.id.constraint_layout)
        val clickableViews: List<View> =
            listOf(boxOneText, boxTwoText, boxThreeText,
                boxFourText, boxFiveText, rootConstraintLayout)
        for (item in clickableViews) {
            item.setOnClickListener { makeColored(it) }
    }
}