package com.chinalwb.are;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;

/**
 * All Rights Reserved.
 *
 * @author Wenbin Liu
 */
public class Util {

    /**
     * Toast message.
     */
    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * @param s
     */
    public static void log(String s) {
        Log.d("CAKE", s);
    }

    /**
     * Returns the line number of current cursor.
     *
     * @param editText
     * @return
     */
    public static int getCurrentCursorLine(EditText editText) {
        int selectionStart = Selection.getSelectionStart(editText.getText());
        Layout layout = editText.getLayout();

        if (null == layout) {
            return -1;
        }
        if (selectionStart != -1) {
            return layout.getLineForOffset(selectionStart);
        }

        return -1;
    }

    /**
     * Returns the selected area line numbers.
     *
     * @param editText
     * @return
     */
    public static int[] getCurrentSelectionLines(EditText editText) {
        Editable editable = editText.getText();
        int selectionStart = Selection.getSelectionStart(editable);
        int selectionEnd = Selection.getSelectionEnd(editable);
        Layout layout = editText.getLayout();

        int[] lines = new int[2];
        if (selectionStart != -1) {
            int startLine = layout.getLineForOffset(selectionStart);
            lines[0] = startLine;
        }

        if (selectionEnd != -1) {
            int endLine = layout.getLineForOffset(selectionEnd);
            lines[1] = endLine;
        }

        return lines;
    }

    /**
     * Returns the line start position of the current line (which cursor is focusing now).
     *
     * @param editText
     * @return
     */
    public static int getThisLineStart(EditText editText, int currentLine) {
        Layout layout = editText.getLayout();
        int start = 0;
        if (currentLine > 0) {
            start = layout.getLineStart(currentLine);
            if (start > 0) {
                String text = editText.getText().toString();
                char lastChar = text.charAt(start - 1);
                while (lastChar != '\n') {
                    if (currentLine > 0) {
                        currentLine--;
                        start = layout.getLineStart(currentLine);
                        if (start > 1) {
                            start--;
                            lastChar = text.charAt(start);
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return start;
    }

    /**
     * Returns the line end position of the current line (which cursor is focusing now).
     *
     * @param editText
     * @return
     */
    public static int getThisLineEnd(EditText editText, int currentLine) {
        Layout layout = editText.getLayout();
        if (-1 != currentLine) {
            return layout.getLineEnd(currentLine);
        }
        return -1;
    }

    /**
     * Gets the pixels by the given number of dp.
     *
     * @param context
     * @param dp
     * @return
     */
    public static int getPixelByDp(Context context, int dp) {
        int pixels = dp;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        pixels = (int) (displayMetrics.density * dp + 0.5);
        return pixels;
    }

    /**
     * Returns the screen width and height.
     *
     * @param context
     * @return
     */
    public static int[] getScreenWidthAndHeight(Context context) {
        Point outSize = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getSize(outSize);

        int[] widthAndHeight = new int[2];
        widthAndHeight[0] = outSize.x;
        widthAndHeight[1] = outSize.y;
        return widthAndHeight;
    }

    /**
     * Returns the color in string format.
     *
     * @param intColor
     * @param containsAlphaChannel
     * @param removeAlphaFromResult
     * @return
     */
    public static String colorToString(int intColor, boolean containsAlphaChannel, boolean removeAlphaFromResult) {
        String strColor = String.format("#%06X", 0xFFFFFF & intColor);
        if (containsAlphaChannel) {
            strColor = String.format("#%06X", 0xFFFFFFFF & intColor);
            if (removeAlphaFromResult) {
                StringBuffer buffer = new StringBuffer(strColor);
                buffer.delete(1, 3);
                strColor = buffer.toString();
            }
        }

        return strColor;
    }

    public static Bitmap scaleBitmapToFitWidth(Bitmap bitmap, int maxWidth) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int newWidth = maxWidth;
        int newHeight = maxWidth * h / w;
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth / w);
        float scaleHeight = ((float) newHeight / h);
        if (w < maxWidth * 0.2) {
            return bitmap;
        }
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    public static Bitmap mergeBitmaps(Bitmap background, Bitmap foreground) {
        if( background == null ) {
            return null;
        }

        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();

        //create the new blank bitmap
        Bitmap newBitmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        //draw bg into
        cv.drawBitmap(background, 0, 0, null);

        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        int fgLeft = (bgWidth - fgWidth) / 2;
        int fgTop = (bgHeight - fgHeight) / 2;

        //draw fg into
        cv.drawBitmap(foreground, fgLeft, fgTop, null);
        //save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);
        //store
        cv.restore();
        return newBitmap;
    }

    public static class GetPathFromUri4kitkat {

        /**
         * For Android 4.4
         */
        @SuppressLint("NewApi")
        public static String getPath(final Context context, final Uri uri) {

            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    if (id.startsWith("raw:")) {
                        return id.substring(4);
                    }
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] { split[1] };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context
         *            The context.
         * @param uri
         *            The Uri to query.
         * @param selection
         *            (Optional) Filter used in the query.
         * @param selectionArgs
         *            (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        public static String getDataColumn(Context context, Uri uri, String selection,
                                           String[] selectionArgs) {

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = { column };

            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }

        /**
         * @param uri
         *            The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri
         *            The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri
         *            The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }
    }

    public static void hideKeyboard(View view, Context context) {
        if (view != null && context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getBounds().width();
        int h = drawable.getBounds().height();
        Bitmap bitmap = Bitmap.createBitmap(
                w,
                h,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}
