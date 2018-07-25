package com.chinalwb.are.spans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

import com.chinalwb.are.Constants;

public class AreImageSpan extends ImageSpan implements ARE_Clickable_Span {

	public enum ImageType {
		URI,
		URL,
		RES,
	}

	private Context mContext;

	private Uri mUri;

	private String mUrl;

	private int mResId;

	public AreImageSpan(Context context, Bitmap bitmapDrawable, Uri uri) {
		super(context, bitmapDrawable);
		this.mContext = context;
		this.mUri = uri;
	}

	public AreImageSpan(Context context, Bitmap bitmapDrawable, String url) {
		super(context, bitmapDrawable);
		this.mContext = context;
		this.mUrl = url;
	}

	public AreImageSpan(Context context, Drawable drawable, String url) {
		super(drawable, url);
		this.mContext = context;
		this.mUrl = url;
	}

//	public AreImageSpan(Context context, Bitmap bitmapDrawable, int resId) {
//		super(context, bitmapDrawable);
//		this.mContext = context;
//	}

	public AreImageSpan(Context context, int resId) {
	    super(context, resId);
	    this.mContext = context;
	    this.mResId = resId;
    }

    public AreImageSpan(Context context, Uri uri) {
		super(context, uri);
		this.mContext = context;
		this.mUri = uri;
	}

//
//	@Override
//	public Drawable getDrawable() {
//        Log.e("debug", "get drawale called");
//        if (mDrawable != null) {
//	        return mDrawable;
//        }
//		mDrawable = super.getDrawable();
//		int width = mDrawable.getIntrinsicWidth();
//		int height = mDrawable.getIntrinsicHeight();
//
//		int[] widthAndHeight = Util.getScreenWidthAndHeight(this.mContext);
//		int screenWidth = widthAndHeight[0];
//		int screenHeight = widthAndHeight[1];
//
//		float maxExpectedWidth = screenWidth * sMaxPercentage;
//		float scaleWidth = 1f;
//		if (width > maxExpectedWidth) {
//			scaleWidth = maxExpectedWidth / width;
//		}
//
//		float maxExpectedHeight = screenHeight * sMaxPercentage;
//		float scaleHeigh = 1f;
//		if (height > maxExpectedHeight) {
//			scaleHeigh = maxExpectedHeight / height;
//		}
//
//		float scale = scaleWidth < scaleHeigh ? scaleWidth : scaleHeigh;
//
//		width = (int) (width * scale);
//		height = (int) (height * scale);
//        mDrawable.setBounds(0, 0, width, height);
//
//		return mDrawable;
//	}
//
//	@Override
//	public void draw(Canvas canvas, CharSequence text, int start, int end,
//					 float x, int top, int y, int bottom, Paint paint) {
//		Drawable b = getCachedDrawable();
//		canvas.save();
//
//        Log.e("debug", "draw called");
//		//
//		// Take consideration of the line spacing
//		// When line spacing is bigger than 1
//		// The value of bottom will be bigger than the y
//		// For example:
//		// When I set the line spacing to 1.2
//		// Then I type at the first line and then insert an image at the 2nd
//		// Line, then I will get (by debugging):
//		// y = 518
//		// bottom = 614
//		// (The height of the image I insert is 480)
//		//
//		// To analysis this scenario:
//		// 1. The height of the 1st line is 38 - this is get by debugging
//		// 2. The height of the image is 480, this is get by reading the
//		//    attributes of the Drawable
//		// 3. 480 * 1.2 = 576 ----> 96 bigger than line spacing = 1
//		// 4. 576 + 38 = 614 = bottom
//		//
//		// So the height of the line which contains the image,
//		// is getting 1.2 times larger
//		// So we need to calculate the correct transfer of y direction
//		//
//		// by this algorithm:
//		// int transY = bottom - b.getBounds().bottom - (bottom - y) / 2;
//		// 614 - 480 - (614 - 518) / 2 = 86
//		// 86 - 38 (1st line height) = 48
//		// With this algorithm, we vertical_center aligned the image to the line
//		//
//		//
//		// To be noted:
//		// If change the line spacing to the default value: 1
//		// The scenario will become like:
//		// 1. first line height = 38, which is the same
//		// 2. the image height is 480, which is the same
//		// 3. 38 + 480 = 518 = y
//		// 4. debug it and see bottom is the same as y
//		// 5. so bottom - y = 0, (bottom - y) / 2 will be 0
//		//    and this has no side effect
//		int transY = bottom - b.getBounds().bottom - (bottom - y) / 2;
//		canvas.translate(x, transY);
//		b.draw(canvas);
//		canvas.restore();
//	}
//
//	private Drawable getCachedDrawable() {
//        Log.e("debug", "get cached drawable called");
//		WeakReference<Drawable> wr = mDrawableRef;
//		Drawable d = null;
//
//		if (wr != null)
//			d = wr.get();
//
//		if (d == null) {
//			d = getDrawable();
//			mDrawableRef = new WeakReference<Drawable>(d);
//		}
//
//		return d;
//	}
//
//	private WeakReference<Drawable> mDrawableRef;

    @Override
    public String getSource() {
	    if (this.mUri != null) {
            return this.mUri.toString();
        } else if (this.mUrl != null) {
	    		return this.mUrl;
		} else {
	        return Constants.EMOJI + "|" + this.mResId;
        }
    }

    public ImageType getImageType() {
		if (this.mUri != null) {
			return ImageType.URI;
		} else if (this.mUrl != null) {
			return ImageType.URL;
		} else {
			return ImageType.RES;
		}
	}

	public Uri getUri() {
		return this.mUri;
	}

	public String getURL() {
		return this.mUrl;
	}


	public int getResId() {
		return this.mResId;
	}

}
