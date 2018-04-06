package com.chinalwb.are;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

/**
 * All Rights Reserved.
 * 
 * @author Wenbin Liu
 * 
 */
public class MainActivity extends AppCompatActivity {

	private AREditor arEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		this.arEditor = (AREditor) this.findViewById(R.id.areditor);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int menuId = item.getItemId();
		if (menuId == R.id.action_save) {
			String html = this.arEditor.getHtml();
			saveHtml(html);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressLint("SimpleDateFormat") private void saveHtml(String html) {
		try {
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
					Util.toast(this, "Cannot create file at: " + filePath);
					return;
				}
			}
			
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(html);
			fileWriter.close();
			
			Util.toast(this, fileName + " has been saved at " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
			Util.toast(this, "Run into error: " + e.getMessage());
		}
	}
	
	
	/**
	 * The layout file.
	 */
	private int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.arEditor.onActivityResult(requestCode, resultCode, data);
	}
}
