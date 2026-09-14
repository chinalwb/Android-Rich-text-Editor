package com.chinalwb.are;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
     * Returns the paragraph (logical line) index of the given offset.
     *
     * <p>A paragraph is the text between two {@code '\n'} characters. This is
     * deliberately not the same as a {@link Layout} line, which is a <i>visual</i>
     * line and therefore changes when the text wraps. Block styles (list / quote /
     * alignment / indent) apply to whole paragraphs, so they must not be computed
     * from the visual layout.</p>
     *
     * @param text   the text to inspect
     * @param offset an offset inside {@code text}
     * @return the zero based paragraph index, or -1 if the input is unusable
     */
    public static int getParagraphIndex(CharSequence text, int offset) {
        if (null == text) {
            return -1;
        }
        if (offset < 0) {
            return -1;
        }
        if (offset > text.length()) {
            offset = text.length();
        }

        int paragraph = 0;
        for (int i = 0; i < offset; i++) {
            if (text.charAt(i) == Constants.CHAR_NEW_LINE) {
                paragraph++;
            }
        }
        return paragraph;
    }

    /**
     * Returns the offset the given paragraph starts at.
     *
     * @param text           the text to inspect
     * @param paragraphIndex the zero based paragraph index
     * @return the start offset, clamped into {@code text}
     */
    public static int getParagraphStart(CharSequence text, int paragraphIndex) {
        if (null == text || paragraphIndex <= 0) {
            return 0;
        }

        int paragraph = 0;
        int length = text.length();
        for (int i = 0; i < length; i++) {
            if (text.charAt(i) == Constants.CHAR_NEW_LINE) {
                paragraph++;
                if (paragraph == paragraphIndex) {
                    return i + 1;
                }
            }
        }
        return length;
    }

    /**
     * Returns the offset the given paragraph ends at.
     *
     * <p>Consistent with {@link Layout#getLineEnd(int)}, the returned offset is
     * <i>after</i> the trailing {@code '\n'} when the paragraph has one.</p>
     *
     * @param text           the text to inspect
     * @param paragraphIndex the zero based paragraph index
     * @return the end offset, clamped into {@code text}
     */
    public static int getParagraphEnd(CharSequence text, int paragraphIndex) {
        if (null == text) {
            return 0;
        }

        int length = text.length();
        int start = getParagraphStart(text, paragraphIndex);
        for (int i = start; i < length; i++) {
            if (text.charAt(i) == Constants.CHAR_NEW_LINE) {
                return i + 1;
            }
        }
        return length;
    }

    /**
     * Returns the number of paragraphs in the given text.
     *
     * @param text the text to inspect
     * @return the paragraph count, at least 1 for a non null text
     */
    public static int getParagraphCount(CharSequence text) {
        if (null == text) {
            return 0;
        }

        int count = 1;
        int length = text.length();
        for (int i = 0; i < length; i++) {
            if (text.charAt(i) == Constants.CHAR_NEW_LINE && i != length - 1) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the paragraph number the cursor is currently in.
     *
     * <p>The value is derived from the text itself rather than from the
     * {@link Layout}, so it is correct for wrapped lines and is available before
     * the first layout pass has happened (or right after an edit, when the layout
     * has not been rebuilt yet).</p>
     *
     * @param editText
     * @return
     */
    public static int getCurrentCursorLine(EditText editText) {
        if (null == editText) {
            return -1;
        }
        Editable editable = editText.getText();
        if (null == editable) {
            return -1;
        }

        int selectionStart = Selection.getSelectionStart(editable);
        if (selectionStart == -1) {
            return -1;
        }

        return getParagraphIndex(editable, selectionStart);
    }

    /**
     * Returns the selected area paragraph numbers.
     *
     * @param editText
     * @return
     */
    public static int[] getCurrentSelectionLines(EditText editText) {
        int[] lines = new int[2];
        if (null == editText) {
            return lines;
        }
        Editable editable = editText.getText();
        if (null == editable) {
            return lines;
        }

        int selectionStart = Selection.getSelectionStart(editable);
        int selectionEnd = Selection.getSelectionEnd(editable);

        if (selectionStart != -1) {
            lines[0] = getParagraphIndex(editable, selectionStart);
        }

        if (selectionEnd != -1) {
            lines[1] = getParagraphIndex(editable, selectionEnd);
        }

        return lines;
    }

    /**
     * Returns the start position of the paragraph the cursor is focusing now.
     *
     * @param editText
     * @return
     */
    public static int getThisLineStart(EditText editText, int currentLine) {
        if (null == editText || null == editText.getText()) {
            return 0;
        }
        return getParagraphStart(editText.getText(), currentLine);
    }

    /**
     * Returns the end position of the paragraph the cursor is focusing now.
     *
     * <p>The trailing {@code '\n'} is included, so callers that want the visible
     * content of the paragraph need to step back over it.</p>
     *
     * @param editText
     * @return
     */
    public static int getThisLineEnd(EditText editText, int currentLine) {
        if (-1 == currentLine) {
            return -1;
        }
        if (null == editText || null == editText.getText()) {
            return -1;
        }
        return getParagraphEnd(editText.getText(), currentLine);
    }

    /**
     * Deletes the zero width marker a list item keeps at its start.
     *
     * <p>List items are created by inserting a {@link Constants#ZERO_WIDTH_SPACE_STR}
     * so that an empty item still has a line to draw its bullet or number on. The
     * marker has to go when the item stops being a list item, otherwise it stays
     * behind in the text and one more of them piles up on every toggle.</p>
     *
     * @param editable the text to edit
     * @param offset   the offset the list item started at
     * @return true when a marker was deleted
     */
    public static boolean removeZeroWidthMarker(Editable editable, int offset) {
        if (null == editable || offset < 0 || offset >= editable.length()) {
            return false;
        }

        if (editable.charAt(offset) != Constants.ZERO_WIDTH_SPACE_INT) {
            return false;
        }

        editable.delete(offset, offset + 1);
        return true;
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
        while (!(context instanceof Activity)) {
            context = ((ContextWrapper)context).getBaseContext();
        }
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
        cv.save();
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

                    String externalStoragePath = findExternalStorageDocumentPath(context, type, split[1]);
                    if (externalStoragePath != null) {
                        return externalStoragePath;
                    }
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

        static String findExternalStorageDocumentPath(Context context, String type, String relativePath) {
            File[] externalDirs = context.getExternalFilesDirs(null);
            if (externalDirs == null) {
                return null;
            }

            List<String> externalDirPaths = new ArrayList<>();
            for (File externalDir : externalDirs) {
                if (externalDir != null) {
                    externalDirPaths.add(externalDir.getAbsolutePath());
                }
            }

            return buildExternalStorageDocumentPath(type, relativePath, externalDirPaths.toArray(new String[0]));
        }

        static String buildExternalStorageDocumentPath(String type, String relativePath, String[] externalDirPaths) {
            if (type == null || relativePath == null || externalDirPaths == null) {
                return null;
            }

            for (String externalDirPath : externalDirPaths) {
                if (externalDirPath == null) {
                    continue;
                }
                int androidDataIndex = externalDirPath.indexOf("/Android/data/");
                if (androidDataIndex == -1) {
                    continue;
                }
                String volumeRoot = externalDirPath.substring(0, androidDataIndex);
                if (volumeRoot.toLowerCase().contains(type.toLowerCase())) {
                    return volumeRoot + "/" + relativePath;
                }
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

    public static Bitmap createVideoThumbnail(Context context, Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(context, uri);
            return retriever.getFrameAtTime();
        } catch (RuntimeException e) {
            String path = GetPathFromUri4kitkat.getPath(context, uri);
            if (path == null) {
                return null;
            }
            return android.media.ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
        } finally {
            try {
                retriever.release();
            } catch (Exception ignored) {
            }
        }
    }
}
