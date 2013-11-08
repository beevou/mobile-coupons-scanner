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


public class BeevouEditText extends LinearLayout {

	//constructor 1 required for in-code creation
	 
	protected EditText editBox;
	private String hint;
	private int inputType;
	protected Context theContext;
	protected ViewGroup controlLayout;
	public ImageButton searchButton;
	protected TextView editLabel;
 
	public BeevouEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		theContext = context;
	}
 
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity)getContext()).getLayoutInflater().inflate(R.layout.edit_form_field, this);
		setupViewItems();
	}
 
	private void setupViewItems() {
		editBox = (EditText) findViewById(R.id.editBox);
		controlLayout = (LinearLayout) findViewById(R.id.controlLayout);
		editLabel = (TextView) findViewById(R.id.editLabel);
	}
 
	public void setText(String text) {
		if (text != null &&! text.equals("") &&! text.equals("null"))
			editBox.setText(text);
	}
 
	public String getText() {
		return editBox.getText().toString();
	}
	
	public void setHint(String hintStr)
	{
		hint = hintStr;
		editBox.setHint(hint);
	}
	
	public String getHint()
	{
		return hint;
	}
		
	
	public void setInputType(int value)
	{
		inputType = value;
		editBox.setInputType(value);
	}
	
	public int getInputType()
	{
		return inputType;
	}
	

	
	public void setLabel(String value)
	{
		
		editLabel.setText(value);
		
		
	}
	
	public void setReadOnly(Boolean value)
	{
		if (value == true)
		{
			editBox.setEnabled(false);
		} else {
			editBox.setEnabled(true);
		}
	}
	
	


	
	
}
