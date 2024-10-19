package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val component = findViewById<Component>(R.id.component)
        val view1 = View(this).apply {
            setBackgroundColor(Color.BLUE)
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        val view2 = View(this).apply {
            setBackgroundColor(Color.BLUE)
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        val view3 = View(this).apply {
            setBackgroundColor(Color.BLUE)
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        component.setupRealView(view1, view2, view3)
    }
}
