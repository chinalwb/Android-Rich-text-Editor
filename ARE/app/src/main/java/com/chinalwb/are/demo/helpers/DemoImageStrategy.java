package com.chinalwb.are.demo.helpers;

import android.net.Uri;

import com.chinalwb.are.spans.AreImageSpan;
import com.chinalwb.are.strategies.ImageStrategy;
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_Image;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoImageStrategy implements ImageStrategy {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    @Override
    public void uploadAndInsertImage(Uri uri, ARE_Style_Image areStyleImage) {
        UploadImageTask task = new UploadImageTask(areStyleImage);
        task.upload(uri);
    }

    private static class UploadImageTask {

        private final WeakReference<ARE_Style_Image> areStyleImage;

        UploadImageTask(ARE_Style_Image styleImage) {
            this.areStyleImage = new WeakReference<>(styleImage);
        }

        void upload(Uri uri) {
            ARE_Style_Image styleImage = areStyleImage.get();
            if (styleImage == null) {
                return;
            }

            EXECUTOR.execute(() -> {
                String imageUrl = null;
                if (uri != null) {
                    try {
                        // do upload here ~
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    imageUrl = "https://avatars0.githubusercontent.com/u/1758864?s=460&v=4";
                }

                ARE_Style_Image currentStyleImage = areStyleImage.get();
                if (currentStyleImage == null || imageUrl == null) {
                    return;
                }

                String finalImageUrl = imageUrl;
                currentStyleImage.getEditText().post(() ->
                        currentStyleImage.insertImage(finalImageUrl, AreImageSpan.ImageType.URL));
            });
        }
    }
}
