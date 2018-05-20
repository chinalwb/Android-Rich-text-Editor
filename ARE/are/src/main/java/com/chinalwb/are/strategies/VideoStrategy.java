package com.chinalwb.are.strategies;

import android.net.Uri;

public interface VideoStrategy {

    /**
     * Upload the video to server and return the url of the video at server
     *
     * @param uri
     * @return
     */
    public String uploadVideo(Uri uri);

    /**
     * Upload the video to server and return the url of the video at server
     *
     * @param videoPath
     * @return
     */
    public String uploadVideo(String videoPath);
}
