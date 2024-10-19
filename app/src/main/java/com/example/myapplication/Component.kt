package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Handler
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

class Component : FrameLayout {

    private var firstView: FrameLayout
    private var secondView: FrameLayout
    private var thirdView: FrameLayout
    private val state = mutableMapOf<Int, Boolean>()
    private val viewMap = mutableMapOf<Int, View>()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        LayoutInflater.from(context).inflate(R.layout.component, this)

        firstView = findViewById(R.id.first)
        secondView = findViewById(R.id.second)
        thirdView = findViewById(R.id.third)

        viewMap[1] = firstView
        viewMap[2] = secondView
        viewMap[3] = thirdView

        state[1] = false
        state[2] = false
        state[3] = false
        Handler().postDelayed(Runnable {
            animateWithPosition(1)
        }, 100)

        firstView.setOnClickListener {
            animateWithPosition(1)
        }

        secondView.setOnClickListener {
            animateWithPosition(2)
        }

        thirdView.setOnClickListener {
            animateWithPosition(3)
        }
    }

    private fun animateWithPosition(newBigViewIndex: Int) {
        val oldBigViewIndex = state.filter { it.value }.keys.firstOrNull()
        if (oldBigViewIndex != null) {
            val oldBigView = viewMap[oldBigViewIndex]!!
            val pivotType = getPivotType(oldBigViewIndex, false, newBigViewIndex)
            val pivot = getPivot(oldBigView, pivotType)
            animateScale(oldBigView, pivot, false)
        }

        val newBigView: View = viewMap[newBigViewIndex]!!
        val pivotType = getPivotType(newBigViewIndex, true, newBigViewIndex)
        val pivot = getPivot(newBigView, pivotType)
        animateScale(newBigView, pivot, true)

        animateMidViewTranslation(newBigViewIndex)

        if (oldBigViewIndex != null) {
            state[oldBigViewIndex] = false
        }
        state[newBigViewIndex] = true
    }

    private fun animateMidViewTranslation(newIndex: Int) {
        val margin = dp2Px(50f).toFloat()//activity_main.xml 初始状态1 2 view额外的margin：1、2 margin 51dp ，2、3 margin 1dp ，所以额外margin就是50dp

        val midView = viewMap[2]!!
        val midState = state[2]!!
        if (newIndex == 1) {
            val end = 0f
            if (midState) {
                midView.translationX = end
            } else {
                midView.animate().translationX(end).setInterpolator(null).start()
            }
        }
        if (newIndex == 3) {
            val end = -margin
            if (midState) {
                midView.translationX = end
            } else {
                midView.animate().translationX(end).setInterpolator(null).start()
            }
        }
    }

    private fun animateScale(view: View, position: Point, biggerOrSmall: Boolean) {
        view.pivotX = position.x.toFloat()
        view.pivotY = position.y.toFloat()

        view.animate()
            .setInterpolator(null)
            .scaleX(if (biggerOrSmall) 1.5f else 1f)
            .scaleY(if (biggerOrSmall) 1.5f else 1f)
            .start()
    }

    private val leftTop = 1
    private val rightTop = 2
    private val leftBottom = 3
    private val rightBottom = 4

    private fun getPivotType(index: Int, biggerOrSmall: Boolean, newBigIndex: Int): Int {
        when (index) {
            1 -> return leftBottom
            3 -> return rightBottom
            2 -> {
                return if (biggerOrSmall) {
                    if (state[index - 1] == true) {
                        rightBottom
                    } else {
                        leftBottom
                    }
                } else {
                    if (newBigIndex > index) {
                        leftBottom
                    } else {
                        rightBottom
                    }
                }
            }

        }

        return leftTop
    }

    private fun getPivot(view: View, type: Int): Point {
        if (type == leftTop) {
            return Point(0, 0)
        }
        if (type == rightTop) {
            return Point(view.height, 0)
        }
        if (type == leftBottom) {
            return Point(0, view.height)
        }
        if (type == rightBottom) {
            return Point(view.width, view.height)
        }

        return Point(0, 0)
    }

    private fun dp2Px(dp: Float): Int {
        return (dp * getDisplayMetrics().density + 0.5).toInt()
    }

    private fun getDisplayMetrics(): DisplayMetrics {
        return Resources.getSystem().displayMetrics
    }

    fun setupRealView(view1: View, view2: View, view3: View) {
        firstView.addView(view1)
        secondView.addView(view2)
        thirdView.addView(view3)
    }
}