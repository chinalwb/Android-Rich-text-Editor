package com.chinalwb.are.emojipanel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * All Rights Reserved.
 * 
 * @author Wenbin Liu
 *
 */
public class Util {

  /**
   * Toast message.
   */
  public static void toast(Context context, String msg) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
  }
  
  /**
   * 
   * @param s
   */
  public static void log(String s) {
    Log.d("CAKE", s);
  }
  
  /**
   * Returns the line number of current cursor.
   * 
   * @param editText
   * @return
   */
  public static int getCurrentCursorLine(EditText editText) {
    int selectionStart = Selection.getSelectionStart(editText.getText());
    Layout layout = editText.getLayout();

    if (null == layout) {
    	return -1;
    }
    if (selectionStart != -1) {
      return layout.getLineForOffset(selectionStart);
    }

    return -1;
  }
  
  /**
   * Returns the selected area line numbers.
   * 
   * @param editText
   * @return
   */
  public static int[] getCurrentSelectionLines(EditText editText) {
	Editable editable = editText.getText();
    int selectionStart = Selection.getSelectionStart(editable);
    int selectionEnd = Selection.getSelectionEnd(editable);
    Layout layout = editText.getLayout();

    int[] lines = new int[2];
    if (selectionStart != -1) {
      int startLine = layout.getLineForOffset(selectionStart);
      lines[0] = startLine;
    }
    
    if (selectionEnd != -1) {
    	int endLine = layout.getLineForOffset(selectionEnd);
    	lines[1] = endLine;
    }

    return lines;
  }

  /**
   * Returns the line start position of the current line (which cursor is focusing now).
   * 
   * @param editText
   * @return
   */
  public static int getThisLineStart(EditText editText, int currentLine) {
    Layout layout = editText.getLayout();
    int start = 0;
    if (currentLine > 0) {
      start = layout.getLineStart(currentLine);
      if (start > 0) {
        String text = editText.getText().toString();
        char lastChar = text.charAt(start - 1);
        while (lastChar != '\n') {
          if (currentLine > 0) {
            currentLine--;
            start = layout.getLineStart(currentLine);
            if (start > 1) {
              start--;
              lastChar = text.charAt(start);
            }
            else {
              break;
            }
          }
        }
      }
    }
    return start;
  }

  /**
   * Returns the line end position of the current line (which cursor is focusing now).
   * 
   * @param editText
   * @return
   */
  public static int getThisLineEnd(EditText editText, int currentLine) {
    Layout layout = editText.getLayout();
    if (-1 != currentLine) {
      return layout.getLineEnd(currentLine);
    }
    return -1;
  }
  
  /**
   * Gets the pixels by the given number of dp.
   * 
   * @param context
   * @param dp
   * @return
   */
  public static int getPixelByDp(Context context, int dp) {
	  int pixels = dp;
	  DisplayMetrics displayMetrics = new DisplayMetrics();
	  ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	  pixels = (int) (displayMetrics.density * dp + 0.5);
	  return pixels;
  }
  
  /**
   * Returns the screen width and height.
   * 
   * @param context
   * @return
   */
  public static int[] getScreenWidthAndHeight(Context context) {
	 Point outSize = new Point();
	 WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	 Display display = windowManager.getDefaultDisplay();
	 display.getSize(outSize);
	 
	 int[] widthAndHeight = new int[2];
	 widthAndHeight[0] = outSize.x;
	 widthAndHeight[1] = outSize.y;
	 return widthAndHeight;
  }

  /**
   * Returns the color in string format.
   *
   * @param intColor
   * @param containsAlphaChannel
   * @param removeAlphaFromResult
   * @return
   */
  public static String colorToString(int intColor, boolean containsAlphaChannel, boolean removeAlphaFromResult) {
    String strColor = String.format("#%06X", 0xFFFFFF & intColor);
    if (containsAlphaChannel) {
      strColor = String.format("#%06X", 0xFFFFFFFF & intColor);
      if (removeAlphaFromResult) {
        StringBuffer buffer = new StringBuffer(strColor);
        buffer.delete(1, 3);
        strColor = buffer.toString();
      }
    }

    return strColor;
  }
}
