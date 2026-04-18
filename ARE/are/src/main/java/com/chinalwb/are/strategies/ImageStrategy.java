package com.chinalwb.are.strategies;

import android.net.Uri;

import com.chinalwb.are.styles.toolitems.styles.ARE_Style_Image;

public interface ImageStrategy {

    /**
     * Upload the video to server and return the url of the video at server.
     * After that done, you need to call
     * {@link ARE_Style_Image#insertImage(Object, com.chinalwb.are.spans.AreImageSpan.ImageType)}
     * to insert the url on server to ARE
     *
     * @param uri
     * @param areStyleImage used to insert the url on server to ARE
     */
    void uploadAndInsertImage(Uri uri, ARE_Style_Image areStyleImage);
}
