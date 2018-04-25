package com.chinalwb.are.spans;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.style.ImageSpan;

public class AreVideoSpan extends ImageSpan implements ARE_Span {
	private Context mContext;

	private Uri mVideoPath;

	public AreVideoSpan(Context context, Bitmap bitmapDrawable, Uri videoPath) {
		super(context, bitmapDrawable);
		this.mContext = context;
		this.mVideoPath = videoPath;
	}

	@Override
	public String getHtml() {
		StringBuffer htmlBuffer = new StringBuffer("<video src=\"");
		htmlBuffer.append(mVideoPath.getPath());
		htmlBuffer.append("\" controls=\"controls\">");
		htmlBuffer.append("您的浏览器不支持 video 标签。</video>");
		return htmlBuffer.toString();
	}
}
