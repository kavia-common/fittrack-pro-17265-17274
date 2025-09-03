package org.example.app.ui.charts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import org.example.app.R
import kotlin.math.max

/**
 * PUBLIC_INTERFACE
 * MiniBarChart
 *
 * Minimal bar chart view to visualize last 7 days steps.
 * setData accepts a list of 7 integers.
 */
class MiniBarChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var data: List<Int> = emptyList()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.colorPrimary)
        style = Paint.Style.FILL
    }
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.surfaceVariant)
        style = Paint.Style.FILL
    }

    // PUBLIC_INTERFACE
    fun setData(values: List<Int>) {
        /** Sets the weekly steps data for the chart. Expects 7 values. */
        data = values.takeLast(7)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
        if (data.isEmpty()) return

        val maxVal = max(1, data.maxOrNull() ?: 1)
        val gap = width / 40f
        val barWidth = (width - gap * 8) / 7f
        var x = gap
        for (v in data) {
            val h = (v.toFloat() / maxVal) * (height * 0.85f)
            canvas.drawRoundRect(
                x,
                height - h - gap,
                x + barWidth,
                height - gap,
                barWidth * 0.2f,
                barWidth * 0.2f,
                paint
            )
            x += barWidth + gap
        }
    }
}
