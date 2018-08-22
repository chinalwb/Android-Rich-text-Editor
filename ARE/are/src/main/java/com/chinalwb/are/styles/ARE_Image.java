package com.chinalwb.are.styles;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ImageSpan;
import android.util.Log;
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
import com.chinalwb.are.styles.windows.ImageSelectDialog;
import com.chinalwb.are.glidesupport.GlideApp;
import com.chinalwb.are.glidesupport.GlideRequests;

import static com.chinalwb.are.spans.AreImageSpan.ImageType;

public class ARE_Image implements IARE_Style, IARE_Image {

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

	/**
	 * Open system image chooser page.
	 */
	private void openImageChooser() {
		ImageSelectDialog dialog = new ImageSelectDialog(mContext, this, ARE_Toolbar.REQ_IMAGE);
		dialog.show();
	}

	/**
	 *
	 */
	public void insertImage(final Object src, final ImageType type) {
		// Note for a possible bug:
		// There may be a possible bug here, it is related to:
		//   https://issuetracker.google.com/issues/67102093
		// But I forget what the real use case is, just remember that
		// When working on the feature, there was a crash bug
		//
		// That's why I introduce the method below:
	    // this.mEditText.useSoftwareLayerOnAndroid8();
		//
		// However, with this setting software layer, there is another
		// bug which is when inserting a few (2~3) images, there will
		// be a warning:
		//
		// AREditText not displayed because it is too large to fit into a software layer (or drawing cache), needs 17940960 bytes, only 8294400 available
		//
		// And then the EditText becomes an entire white nothing displayed
		//
		// So in temporary, I commented out this method invoke to prevent this known issue
		// When someone run into the crash bug caused by this on Android 8
		// I can then find out a solution to cover both cases
		SimpleTarget myTarget = new SimpleTarget<Bitmap>() {
			@Override
			public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
				if (bitmap == null) { return; }

                bitmap = Util.scaleBitmapToFitWidth(bitmap, sWidth);
                ImageSpan imageSpan = null;
                if (type == ImageType.URI) {
                    imageSpan = new AreImageSpan(mContext, bitmap, ((Uri) src));
				} else if (type == ImageType.URL) {
					imageSpan = new AreImageSpan(mContext, bitmap, ((String) src));
				}
				if (imageSpan == null) { return; }
				insertSpan(imageSpan);
			}
		};

        if (type == ImageType.URI) {
            sGlideRequests.asBitmap().load((Uri) src).centerCrop().into(myTarget);
        } else if (type == ImageType.URL) {
        	    sGlideRequests.asBitmap().load((String) src).centerCrop().into(myTarget);
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
