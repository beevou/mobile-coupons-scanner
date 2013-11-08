package com.beevou.android.scanner.widgets;

import java.util.Calendar;

import com.beevou.android.scanner.R;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;


public class BeevouEditDate extends BeevouEditText implements OnDateSetListener {

	//constructor 1 required for in-code creation
	static final int DATE_DIALOG_ID = 0;
	private int mYear;
    private int mMonth;
    private int mDay;
 
	public BeevouEditDate(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
	}
 
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		
		ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
		
		searchButton.setBackgroundResource(R.drawable.corporate_edit_date_button);
		
		searchButton.setVisibility(View.VISIBLE);
		
		
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
		
        
        
       
		searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showDialog();
            }
        });
		
		
		editBox.setMaxLines(1);
		editBox.setKeyListener(null);

	}
	
	
	public void showDialog() {
        DatePickerDialog dialog = new DatePickerDialog(theContext, this, mYear, mMonth, mDay);
        dialog.show();
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
	
	
	private void updateDisplay() {
		
		
        editBox.setText(new StringBuilder()
        // Month is 0 based so add 1
        .append(mMonth + 1).append("-")
        .append(mDay).append("-")
        .append(mYear).append(" "));
            
    }

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        updateDisplay();
		
	}
	

	
	
}
