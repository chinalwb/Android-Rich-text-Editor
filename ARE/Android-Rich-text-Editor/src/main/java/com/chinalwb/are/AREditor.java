package com.chinalwb.are;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.chinalwb.are.android.inner.Html;
import com.chinalwb.are.strategies.AtStrategy;
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
	 * Instance Fields Area
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

	/**
	 * The are editor.
	 */
	private AREditText mAre;

	/*
	 * --------------------------------------------
	 * Constructors Area
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
	 * --------------------------------------------
	 *  Business Methods Area
	 * --------------------------------------------
	 */
	/**
	 * Initialization.
	 */
	private void init() {
		LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
		if (null != layoutInflater) {
			layoutInflater.inflate(getLayoutId(), this, true);
		} else {
			return;
		}

		this.initViews();
	} // # End of init()

	private int getLayoutId() {
		return R.layout.areditor;
	}

	/**
	 * Init views.
	 */
	private void initViews() {
		this.mToolbar = this.findViewById(R.id.toolbar);
		this.mAre = this.findViewById(R.id.are);
		this.mToolbar.setEditText(mAre);
	}

	/**
	 * 
	 */
	public String getHtml() {
		StringBuffer html = new StringBuffer();
		html.append("<html>");
		appendAREditText(mAre, html);
		html.append("</html>");
		String htmlContent = html.toString().replaceAll(Constants.ZERO_WIDTH_SPACE_STR_ESCAPE, "");
		System.out.println(htmlContent);
		return htmlContent;
	}

	private static  void appendAREditText(AREditText editText, StringBuffer html) {
		String editTextHtml = Html.toHtml(editText.getEditableText(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL);
		html.append(editTextHtml);
	}

	public AREditText getARE() {
		return this.mAre;
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


	/*
	 * ----------------------
	 * Customization part
	 * ----------------------
	 */
	public void setAtStrategy(AtStrategy atStrategy) {
		this.mToolbar.getmAtStyle().setAtStrategy(atStrategy);
	}

    //构造方法
    public AREditor(Builder builder){
        super(builder.context);
        this.mContext = builder.context;
//        this.layoutRes = builder.layoutRes;
	    init();
	    setAtStrategy(builder.atStrategy);
    }

    //建造器
    public static class Builder {
        private int layoutRes = R.layout.areditor;
        private AtStrategy atStrategy;
        private Context context;

        //Required
        public Builder(Context context) {
            this.context = context;
        }

        //Option
        public Builder setLayoutRes(int res) {
            this.layoutRes = res;
            return this;
        }

        public Builder setAtStrategy(AtStrategy atStrategy) {
            this.atStrategy = atStrategy;
            return this;
        }

        public AREditor build() {
            return new AREditor(this);
        }
    }

}
