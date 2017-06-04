
package com.chinalwb.are;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
