package com.chinalwb.are;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

/**
 * All Rights Reserved.
 * 
 * @author Wenbin Liu
 * 
 */
public class AREditor extends RelativeLayout {

	/*
	 * -------------------------------------------- 
	 * * Instance Fields Area
	 * --------------------------------------------
	 */



	/**
	 * Context.
	 */
	private Context mContext;

	/**
	 * The toolbar.
	 */
	private ARE_Toolbar mToolbar;

	/*
	 * -------------------------------------------- * Constructors Area
	 * --------------------------------------------
	 */

	/**
	 * Constructor.
	 * 
	 * @param context
	 */
	public AREditor(Context context) {
		this(context, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param attrs
	 */
	public AREditor(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public AREditor(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		this.init();
	}

	/*
	 * -------------------------------------------- * Business Methods Area
	 * --------------------------------------------
	 */
	/**
	 * Initialization.
	 */
	private void init() {
		LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
		if (null != layoutInflater) {
			layoutInflater.inflate(R.layout.areditor, this, true);
		} else {
			return;
		}

		this.initViews();
	} // # End of init()

	/**
	 * Init views.
	 */
	private void initViews() {
		this.mToolbar = (ARE_Toolbar) this.findViewById(R.id.toolbar);
	}

	/**
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.mToolbar.onActivityResult(requestCode, resultCode, data);
	} // #End of onActivityResult(..)

}
