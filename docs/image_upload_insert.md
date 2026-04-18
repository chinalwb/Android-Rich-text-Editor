I added a new interface:

```
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
```

And this is a demo implementation:

```
package com.chinalwb.are.demo.helpers;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;

import com.chinalwb.are.spans.AreImageSpan;
import com.chinalwb.are.strategies.ImageStrategy;
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_Image;

import java.lang.ref.WeakReference;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

public class DemoImageStrategy implements ImageStrategy {
    @Override
    public void uploadAndInsertImage(Uri uri, ARE_Style_Image areStyleImage) {
        new UploadImageTask(areStyleImage).executeOnExecutor(THREAD_POOL_EXECUTOR, uri);
    }

    private static class UploadImageTask extends AsyncTask<Uri, Integer, String> {

        WeakReference<ARE_Style_Image> areStyleImage;
        private ProgressDialog dialog;
        UploadImageTask(ARE_Style_Image styleImage) {
            this.areStyleImage = new WeakReference<>(styleImage);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialog == null) {
                dialog = ProgressDialog.show(
                        areStyleImage.get().getEditText().getContext(),
                        "",
                        "Uploading image. Please wait...",
                        true);
            } else {
                dialog.show();
            }
        }

        @Override
        protected String doInBackground(Uri... uris) {
            if (uris != null && uris.length > 0) {
                try {
                    // do upload here ~
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Returns the image url on server here
                return "https://avatars0.githubusercontent.com/u/1758864?s=460&v=4";
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog != null) {
                dialog.dismiss();
            }
            if (areStyleImage.get() != null) {
                areStyleImage.get().insertImage(s, AreImageSpan.ImageType.URL);
            }
        }
    }
}

```

And set it to are:

```
com.chinalwb.are.AREditText#setImageStrategy
```

Sample usage here: 

```
com.chinalwb.are.demo.ARE_DefaultToolbarActivity
```

And here is demo a video:
![imageupload](https://user-images.githubusercontent.com/1758864/50088523-3ad86980-023e-11e9-952f-17726703fd6b.gif)

Hope this helps you for uploading and inserting an image from local.
