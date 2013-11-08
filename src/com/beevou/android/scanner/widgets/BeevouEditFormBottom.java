package com.beevou.android.scanner.widgets;

import com.beevou.android.scanner.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class BeevouEditFormBottom extends LinearLayout {

	//constructor 1 required for in-code creation
	 
	
	protected Context theContext;
	
 
	public BeevouEditFormBottom(Context context, AttributeSet attrs) {
		super(context, attrs);
		theContext = context;
	}
 
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity)getContext()).getLayoutInflater().inflate(R.layout.edit_form_bottom, this);
	}
 
	
	


	
	
}
