package com.chinalwb.are.demo;

import android.annotation.SuppressLint;
import android.app.Activity;

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
            File rootDir = activity.getExternalFilesDir("ARE");
            if (rootDir == null) {
                com.chinalwb.are.Util.toast(activity, "Cannot access app external files directory");
                return;
            }
            String filePath = rootDir.getAbsolutePath() + File.separator;
            File dir = new File(filePath);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    com.chinalwb.are.Util.toast(activity, "Cannot create directory at: " + filePath);
                    return;
                }
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
