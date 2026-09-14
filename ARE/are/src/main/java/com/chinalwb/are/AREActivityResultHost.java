package com.chinalwb.are;

import android.content.Intent;
import android.net.Uri;

public interface AREActivityResultHost {

    void pickImage(AREActivityResultCallback<Uri> callback);

    void pickVideo(AREActivityResultCallback<Uri> callback);

    void launchAtPicker(Intent intent, AREActivityResultCallback<Intent> callback);

    void launchVideoPlayer(Intent intent, AREActivityResultCallback<Intent> callback);
}
