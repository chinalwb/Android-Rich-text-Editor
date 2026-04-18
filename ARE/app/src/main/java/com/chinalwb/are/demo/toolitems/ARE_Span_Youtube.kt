package com.chinalwb.are.demo.toolitems

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color.TRANSPARENT
import android.graphics.Paint
import android.text.style.ReplacementSpan
import com.chinalwb.are.spans.ARE_Clickable_Span
import com.chinalwb.are.spans.ARE_Span


class ARE_Span_Youtube (private val icon : Bitmap,
                        private val title : String,
                        private val youtubeUrl : String,
                        private val color : Int)
    : ReplacementSpan(), ARE_Span, ARE_Clickable_Span {

    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return paint.measureText(text, start, end).toInt() + icon.width
    }

    override fun draw(canvas: Canvas, text: CharSequence?,
                      start: Int, end: Int,
                      x: Float, top: Int,
                      y: Int, bottom: Int,
                      paint: Paint) {
        canvas.drawBitmap(icon, x, top.toFloat() + 5, paint)
        paint.color = TRANSPARENT
        val width = paint.measureText(text, start, end)
        canvas.drawRect(x, top.toFloat(), x + width, bottom.toFloat(), paint)
        paint.color = TRANSPARENT or color
        canvas.drawText(text.toString(), start, end, x + icon.width, y.toFloat(), paint)
    }

    override fun getHtml(): String = "<iframe tag=\"youtube\" title=\"$title\" src=\"$youtubeUrl\" />"


}