package com.beevou.android.scanner.widgets;

import com.beevou.android.scanner.R;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class BeevouEditSearch extends BeevouEditText {

	//constructor 1 required for in-code creation
	 
 
	public BeevouEditSearch(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
	}
 
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		
		ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
		
		searchButton.setVisibility(View.VISIBLE);
		
		editBox.setMaxLines(1);
		
		editBox.setKeyListener(null);
		

	}
	
	@Override
	public void setReadOnly(Boolean value)
	{
		super.setReadOnly(value);
		
		if (value == true)
		{
			searchButton.setVisibility(View.GONE);
		}
	}
	

	
	
}
