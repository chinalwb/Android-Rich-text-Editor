package com.chinalwb.are.demo.toolitems

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.chinalwb.are.AREditText
import com.chinalwb.are.Util
import com.chinalwb.are.demo.R
import com.chinalwb.are.styles.ARE_Emoji
import com.chinalwb.are.styles.IARE_Style
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Abstract
import com.chinalwb.are.styles.toolitems.IARE_ToolItem_Updater
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_Link

class ARE_ToolItem_Youtube : ARE_ToolItem_Abstract() {

    override fun getToolItemUpdater(): IARE_ToolItem_Updater? {
        return null
    }

    override fun getStyle(): IARE_Style {
        return mStyle ?: ARE_Style_Youtube(editText, mToolItemView as ImageView)
    }

    override fun getView(context: Context?): View? {
        return mToolItemView ?: {
            //
            // A full colour brand mark, so it is built by hand instead of through
            // createToolItemView - but on the same metrics, so it still lines up
            // with the rest of the toolbar.
            val imageView = ImageView(context)
            val size = Util.getPixelByDp(context, TOOL_ITEM_SIZE_DP)
            val params = LinearLayout.LayoutParams(size, size)
            imageView.layoutParams = params
            val padding = Util.getPixelByDp(context, TOOL_ITEM_PADDING_DP)
            imageView.setPadding(padding, padding, padding, padding)
            imageView.setImageResource(R.drawable.youtube)
            imageView.setBackgroundResource(
                com.chinalwb.are.R.drawable.are_tool_item_background)
            imageView.contentDescription =
                context?.getString(R.string.demo_tool_youtube)
            imageView.bringToFront()
            this.mToolItemView = imageView
            this.mToolItemView
        }()
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        return
    }
}