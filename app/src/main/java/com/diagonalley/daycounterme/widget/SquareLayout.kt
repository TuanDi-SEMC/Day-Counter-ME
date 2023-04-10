package com.diagonalley.daycounterme.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class SquareLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}