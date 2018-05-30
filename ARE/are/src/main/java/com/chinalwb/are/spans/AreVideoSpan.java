package com.chinalwb.are.spans;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.text.style.ImageSpan;

public class AreVideoSpan extends ImageSpan implements ARE_Span {
	private Context mContext;

	private String mVideoPath;

	private String mVideoUrl;

	public AreVideoSpan(Context context, Bitmap bitmapDrawable, String videoPath, String videoUrl) {
		super(context, bitmapDrawable);
		this.mContext = context;
		this.mVideoPath = videoPath;
		this.mVideoUrl = videoUrl;
	}

	@Override
	public String getHtml() {
		StringBuffer htmlBuffer = new StringBuffer("<video src=\"");
		String path = TextUtils.isEmpty(mVideoUrl) ? mVideoPath : mVideoUrl;
		htmlBuffer.append(path);
		htmlBuffer.append("\" uri=\"");
		htmlBuffer.append(mVideoPath);
		htmlBuffer.append("\" controls=\"controls\">");
//		htmlBuffer.append("您的浏览器不支持 video 标签。");
		htmlBuffer.append("</video>");
		return htmlBuffer.toString();
	}
}
