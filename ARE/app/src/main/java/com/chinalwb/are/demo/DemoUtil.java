package com.chinalwb.are.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import androidx.core.app.ActivityCompat;

import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wliu on 22/08/2018.
 */

public class DemoUtil {

    @SuppressLint("SimpleDateFormat")
    public static void saveHtml(Activity activity, String html) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请授权
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ARE_Toolbar.REQ_VIDEO);
                return;
            }

            String filePath = Environment.getExternalStorageDirectory() + File.separator + "ARE" + File.separator;
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdir();
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss");
            String time = dateFormat.format(new Date());
            String fileName = time.concat(".html");

            File file = new File(filePath + fileName);
            if (!file.exists()) {
                boolean isCreated = file.createNewFile();
                if (!isCreated) {
                    com.chinalwb.are.Util.toast(activity, "Cannot create file at: " + filePath);
                    return;
                }
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(html);
            fileWriter.close();

            com.chinalwb.are.Util.toast(activity, fileName + " has been saved at " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            com.chinalwb.are.Util.toast(activity, "Run into error: " + e.getMessage());
        }
    }
}
