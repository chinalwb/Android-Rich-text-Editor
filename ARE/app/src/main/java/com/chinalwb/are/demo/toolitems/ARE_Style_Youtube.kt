package com.chinalwb.are.demo.toolitems

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import android.text.Editable
import android.text.Spanned
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.chinalwb.are.AREditText
import com.chinalwb.are.demo.R
import com.chinalwb.are.styles.ARE_ABS_FreeStyle

class ARE_Style_Youtube(editText: AREditText, imageView: ImageView) : ARE_ABS_FreeStyle(editText.context) {

    private val HTTP = "http://"
    private val HTTPS = "https://"
    private var mAREditText: AREditText = editText
    private var mImageView: ImageView = imageView

    init {
        setListenerForImageView(this.mImageView)
    }

    override fun setListenerForImageView(imageView: ImageView?) {
        imageView?.setOnClickListener {
            openYoutubeDialog();
        }
    }

    private fun openYoutubeDialog() {
        val activity = mImageView.context as Activity
        val builder = AlertDialog.Builder(activity)

        builder.setTitle("Youtube")

        val layoutInflater = activity.layoutInflater
        val areYoutubeView = layoutInflater.inflate(R.layout.are_youtube, null)

        builder.setView(areYoutubeView)
                .setPositiveButton("OK") { dialog, _ ->
                    val youtubeTitle = areYoutubeView.findViewById<EditText>(R.id.are_youtube_title_edit)
                    val youtubeEdit = areYoutubeView.findViewById<EditText>(R.id.are_youtube_url_edit)
                    val youtubeUrl = youtubeEdit.text.toString()
                    if (TextUtils.isEmpty(youtubeUrl)) {
                        dialog.dismiss()
                    } else {
                        insertYoutube(youtubeTitle.text.toString(), youtubeUrl)
                    }
                }
                .setNegativeButton("Cancel") {
                    dialog, _ -> dialog.dismiss()
                };

        val dialog = builder.create()
        dialog.show()
    }

    private fun insertYoutube(title: String, youtubeUrl: String) {
        if (TextUtils.isEmpty(youtubeUrl)) {
            return
        }

        var url = youtubeUrl
        if (!youtubeUrl.startsWith(HTTP) && !youtubeUrl.startsWith(HTTPS)) {
            url = HTTP + youtubeUrl
        }

        val editable = mAREditText.editableText
        val start = mAREditText.selectionStart
        var end = mAREditText.selectionEnd
        if (start == end) {
            editable.insert(start, title)
            end = start + title.length
        }
        Glide.with(mImageView.context).asBitmap().load(com.chinalwb.are.demo.R.drawable.youtube)
                .apply(RequestOptions().override(50, 50))
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        return true
                    }

                    override fun onResourceReady(bitmap: Bitmap?, model: Any?, target: com.bumptech.glide.request.target.Target<Bitmap>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                        val youtubeColor = Color.RED
                        editable.setSpan(ARE_Span_Youtube(bitmap!!, title, url, youtubeColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        return true
                    }
                })
                .submit()
    }

    override fun getEditText(): EditText {
        return super.getEditText()
    }

    override fun applyStyle(editable: Editable?, start: Int, end: Int) {
        // Do nothing
    }

    override fun getImageView(): ImageView {
        return mImageView
    }

    override fun setChecked(isChecked: Boolean) {
        // Do nothing
    }
}