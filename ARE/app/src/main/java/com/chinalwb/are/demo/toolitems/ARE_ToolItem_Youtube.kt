package com.chinalwb.are.demo.toolitems

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.chinalwb.are.demo.R
import com.chinalwb.are.styles.IARE_Style
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Abstract
import com.chinalwb.are.styles.toolitems.IARE_ToolItem_Updater

class ARE_ToolItem_Youtube : ARE_ToolItem_Abstract() {

    override fun getToolItemUpdater(): IARE_ToolItem_Updater? {
        return null
    }

    override fun getStyle(): IARE_Style {
        return mStyle ?: ARE_Style_Youtube(editText, mToolItemView as ImageView)
    }

    override fun getView(context: Context): View? {
        return mToolItemView ?: {
            mToolItemView = createIcon(context, R.drawable.youtube)
            this.mToolItemView
        }()
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        return
    }
}