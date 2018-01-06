package com.chinalwb.are;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

/**
 * All Rights Reserved.
 * 
 * @author Wenbin Liu
 * 
 */
public class MainActivity extends Activity {

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
			DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(); //new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
			String time = dateFormat.format(new Date());
			String fileName = time.concat(".html");
			String filePath = Environment.getExternalStorageState().concat("/ARE/").concat(fileName);
			this.arEditor.saveHtml(filePath);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
