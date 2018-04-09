package com.chinalwb.are.styles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chinalwb.are.AREditText;
import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreImageSpan;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;
import com.rainliu.glidesupport.GlideApp;
import com.rainliu.glidesupport.GlideRequests;

public class ARE_Image implements IARE_Style {

	private ImageView mInsertImageView;

	private AREditText mEditText;

	private Context mContext;

    private static GlideRequests sGlideRequests;

    private static int sWidth = 0;

    /**
	 *
	 * @param emojiImageView the emoji image view
	 */
	public ARE_Image(ImageView emojiImageView) {
		this.mInsertImageView = emojiImageView;
		this.mContext = emojiImageView.getContext();
		sGlideRequests = GlideApp.with(mContext);
        sWidth = Util.getScreenWidthAndHeight(mContext)[0];
		setListenerForImageView(this.mInsertImageView);
	}

	public void setEditText(AREditText editText) {
		this.mEditText = editText;
	}

	@Override
	public void setListenerForImageView(ImageView imageView) {
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openImageChooser();
			}
		});
	} // #End of setListenerForImageView(..)

	public enum ImageType {
		URI,
		RES,
	}

	/**
	 * Open system image chooser page.
	 */
	private void openImageChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		((Activity) this.mContext).startActivityForResult(intent, ARE_Toolbar.REQ_IMAGE);
	}


	/**
	 *
	 */
	public void insertImage(final Object src, final ImageType type) {
	    this.mEditText.useSoftwareLayerOnAndroid8();
		SimpleTarget myTarget = new SimpleTarget<Bitmap>() {
			@Override
			public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
				if (bitmap == null) { return; }

                bitmap = Util.scaleBitmapToFitWidth(bitmap, sWidth);
                ImageSpan imageSpan = null;
                if (type == ImageType.URI) {
                    imageSpan = new AreImageSpan(mContext, bitmap, ((Uri) src));
				}
				if (imageSpan == null) { return; }
				insertSpan(imageSpan);
			}
		};

        if (type == ImageType.URI) {
            sGlideRequests.asBitmap().load((Uri) src).centerCrop().into(myTarget);
        } else if (type == ImageType.RES) {
            ImageSpan imageSpan = new AreImageSpan(mContext, ((int) src));
            insertSpan(imageSpan);
        }
	}

	private void insertSpan(ImageSpan imageSpan) {
		Editable editable = this.mEditText.getEditableText();
		int start = this.mEditText.getSelectionStart();
		int end = this.mEditText.getSelectionEnd();

		AlignmentSpan centerSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(Constants.CHAR_NEW_LINE);
		ssb.append(Constants.ZERO_WIDTH_SPACE_STR);
		ssb.append(Constants.CHAR_NEW_LINE);
		ssb.append(Constants.ZERO_WIDTH_SPACE_STR);
		ssb.setSpan(imageSpan, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(centerSpan, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		AlignmentSpan leftSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL);
		ssb.setSpan(leftSpan, 3,4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

		editable.replace(start, end, ssb);
	}

	@Override
	public void applyStyle(Editable editable, int start, int end) {
		// Do nothing
	}

	@Override
	public ImageView getImageView() {
		return this.mInsertImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		// Do nothing
	}

	@Override
	public boolean getIsChecked() {
		return false;
	}

	@Override
	public EditText getEditText() {
		return this.mEditText;
	}
}
