package com.chinalwb.are.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chinalwb.are.AREditText;
import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;
import com.chinalwb.are.android.inner.Html;
import com.chinalwb.are.glidesupport.GlideApp;
import com.chinalwb.are.glidesupport.GlideRequests;

public class AreImageGetter implements Html.ImageGetter {

    private Context mContext;

    private TextView mTextView;

    private static GlideRequests sGlideRequests;


    public AreImageGetter(Context context, TextView textView) {
        mContext = context;
        mTextView = textView;
        sGlideRequests = GlideApp.with(mContext);
    }

    @Override
    public Drawable getDrawable(String source) {
        if (source.startsWith(Constants.EMOJI)) {
            String resIdStr = source.substring(6);
            int resId = Integer.parseInt(resIdStr);
            Drawable d = mContext.getResources().getDrawable(resId);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
        } else if (source.startsWith("http")) {
            AreUrlDrawable areUrlDrawable = new AreUrlDrawable(mContext);
            BitmapTarget bitmapTarget = new BitmapTarget(areUrlDrawable, mTextView);
            sGlideRequests.asBitmap().load(source).into(bitmapTarget);
            return areUrlDrawable;
        } else if (source.startsWith("content")) {
            //   content://media/external/images/media/846589
            AreUrlDrawable areUrlDrawable = new AreUrlDrawable(mContext);
            BitmapTarget bitmapTarget = new BitmapTarget(areUrlDrawable, mTextView);
            try {
                Uri uri = Uri.parse(source);
                sGlideRequests.asBitmap().load(uri).into(bitmapTarget);
                return areUrlDrawable;
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        return null;
    }

    private static class BitmapTarget extends SimpleTarget<Bitmap> {
        private final AreUrlDrawable areUrlDrawable;
        private TextView textView;

        private BitmapTarget(AreUrlDrawable urlDrawable, TextView textView) {
            this.areUrlDrawable = urlDrawable;
            this.textView = textView;
        }

        @Override
        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
            bitmap = Util.scaleBitmapToFitWidth(bitmap, Constants.SCREEN_WIDTH);
            int bw = bitmap.getWidth();
            int bh = bitmap.getHeight();
            Rect rect = new Rect(0, 0, bw, bh);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            bitmapDrawable.setBounds(rect);
            areUrlDrawable.setBounds(rect);
            areUrlDrawable.setDrawable(bitmapDrawable);
            AREditText.stopMonitor();
            textView.setText(textView.getText());
            textView.invalidate();
            AREditText.startMonitor();
        }
    }
}


//package com.chinalwb.are.render;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Rect;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.text.style.ImageSpan;
//import android.util.Log;
//import android.widget.TextView;
//
//import com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.transition.Transition;
//import com.chinalwb.are.Constants;
//import com.chinalwb.are.Util;
//import com.chinalwb.are.android.inner.Html;
//import com.chinalwb.are.spans.AreImageSpan;
//import com.chinalwb.are.styles.ARE_Image;
//import com.rainliu.glidesupport.GlideApp;
//import com.rainliu.glidesupport.GlideRequests;
//
//import java.util.WeakHashMap;
//
//public class AreImageGetter implements Html.ImageGetter {
//
//    private Context mContext;
//
//    private TextView mTextView;
//
//    private static GlideRequests sGlideRequests;
//
//    private static WeakHashMap<String, Bitmap> bitmapWeakHashMap = new WeakHashMap<>();
//
//    public AreImageGetter(Context context, TextView textView) {
//        mContext = context;
//        mTextView = textView;
//        sGlideRequests = GlideApp.with(mContext);
//    }
//
//    @Override
//    public Drawable getDrawable(final String source) {
//        if (source.startsWith(Constants.EMOJI)) {
//            String resIdStr = source.substring(6);
//            int resId = Integer.parseInt(resIdStr);
//            Drawable d = mContext.getResources().getDrawable(resId);
//            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//            return d;
//        } else if (source.startsWith("http")) {
//            Bitmap cacheBitmap = bitmapWeakHashMap.get(source);
//            Util.log("cachebitmap is " + cacheBitmap);
//            if (cacheBitmap != null) {
//                cacheBitmap = Util.scaleBitmapToFitWidth(cacheBitmap, Constants.SCREEN_WIDTH);
//                int bw = cacheBitmap.getWidth();
//                int bh = cacheBitmap.getHeight();
//                Rect rect = new Rect(0, 0, bw, bh);
//                BitmapDrawable bitmapDrawable = new BitmapDrawable(cacheBitmap);
//                bitmapDrawable.setBounds(rect);
//                return bitmapDrawable;
//            } else {
//                final AreUrlDrawable areUrlDrawable = new AreUrlDrawable(mContext);
//                SimpleTarget myTarget = new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
//                         bitmapWeakHashMap.put(source, bitmap);
//                         mTextView.setText(mTextView.getText());
//                         mTextView.invalidate();
////                        bitmap = Util.scaleBitmapToFitWidth(bitmap, Constants.SCREEN_WIDTH);
////                        int bw = bitmap.getWidth();
////                        int bh = bitmap.getHeight();
////                        Rect rect = new Rect(0, 0, bw, bh);
////                        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
////                        bitmapDrawable.setBounds(rect);
////                        areUrlDrawable.setBounds(rect);
////                        areUrlDrawable.setDrawable(bitmapDrawable);
////                        mTextView.setText(mTextView.getText());
////                        mTextView.invalidateDrawable(areUrlDrawable);
//                    }
//                };
//                sGlideRequests.asBitmap().load(source).into(myTarget);
//                return areUrlDrawable;
//            }
//        }
//        return null;
//    }
//
////    private static class BitmapTarget extends SimpleTarget<Bitmap> {
////        private final AreUrlDrawable areUrlDrawable;
////        private TextView textView;
////
////        private BitmapTarget(AreUrlDrawable urlDrawable, TextView textView) {
////            this.areUrlDrawable = urlDrawable;
////            this.textView = textView;
////        }
////
////        @Override
////        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
////            bitmap = Util.scaleBitmapToFitWidth(bitmap, Constants.SCREEN_WIDTH);
////            int bw = bitmap.getWidth();
////            int bh = bitmap.getHeight();
////            Rect rect = new Rect(0, 0, bw, bh);
////            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
////            bitmapDrawable.setBounds(rect);
////            areUrlDrawable.setBounds(rect);
////            areUrlDrawable.setDrawable(bitmapDrawable);
////            textView.setText(textView.getText());
////            textView.invalidate();
////        }
////    }
//}
