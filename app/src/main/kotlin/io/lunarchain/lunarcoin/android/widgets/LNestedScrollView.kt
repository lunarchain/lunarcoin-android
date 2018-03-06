package io.lunarchain.lunarcoin.android.widgets

import android.content.Context
import android.graphics.Rect
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet

class LNestedScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    NestedScrollView(context, attrs, defStyleAttr) {

    override fun computeScrollDeltaToGetChildRectOnScreen(rect: Rect?): Int = 0
}