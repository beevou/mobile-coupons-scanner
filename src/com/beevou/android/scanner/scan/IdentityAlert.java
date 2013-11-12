package com.beevou.android.scanner.scan;

import com.beevou.android.scanner.R;
import com.beevou.android.scanner.R.layout;
import com.beevou.android.scanner.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class IdentityAlert extends Activity {
	
	private String emisor;
	private String caducity;
	private String value;
	private String description;
	private String holder;
	private Integer validationCode;
	private String idVoucher;
	private String pin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_identity_alert);
        
        Bundle b = getIntent().getExtras();
        emisor = b.getString("emisor");
		caducity = b.getString("caducity");
		value =	b.getString("value");
		description = b.getString("description");
		holder =  b.getString("holder");
		validationCode  = b.getInt("validationCode");
        idVoucher =  b.getString("idVoucher");
        pin = b.getString("pin");
        
final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);


LinearLayout LY1 = new LinearLayout(this);
LY1.setBackgroundResource(R.drawable.shape_alert_dot_border);
LinearLayout.LayoutParams params02 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
params02.setMargins(10, 10, 10, 10);
LY1.setOrientation(LinearLayout.VERTICAL);
LY1.setLayoutParams(params02);
mainLayout.addView(LY1);

    	TextView  TV5 = new TextView(this, null, android.R.attr.textAppearanceLarge);
    	TV5.setText(R.string.this_voucher_requires_identification_of_the_holder_please_ensure_that_the_holder_of_the_voucher_identify_as_);
    	TV5.setTextColor(Color.RED);
    	TV5.setPadding(20, 5, 20, 5);
    	LY1.addView(TV5);
    	
    	
    	TextView  TV6 = new TextView(this, null, android.R.attr.textAppearanceLarge);
    	TV6.setText(holder);
    	TV6.setTextColor(Color.BLACK);
    	TV6.setPadding(20, 5, 20, 5);
    	LY1.addView(TV6);
    	
    	TextView  TV2 = new TextView(this);
		TV2.setText(R.string.please_ask_the_beneficiary_to_identify_himself_herself_with_an_identity_card_or_driving_licence_to_ensure_that_the_holder_of_the_voucher_match_the_voucher_s_beneficiary_name_);
		TV2.setTextColor(Color.parseColor("#C09853"));
		LY1.addView(TV2);
    	
    	
    	Button t3 = new Button(this);
        t3.setTextAppearance(this, R.style.ButtonText);
    	t3.setBackgroundResource(R.drawable.blue_button);
        t3.setText(R.string.yes_continue);
        LinearLayout.LayoutParams paramst3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
        paramst3.setMargins(10, 10, 10, 10);
        t3.setLayoutParams(paramst3);
        t3.setOnClickListener(new View.OnClickListener() {  //Add a listener for when the button is pressed
            @Override
            public void onClick(View v) {
            	Intent returnIntent = new Intent();
            	 setResult(RESULT_OK,returnIntent);     
            	 finish();
            }
    });
        mainLayout.addView(t3);
        
        Button t4 = new Button(this);
        t4.setTextAppearance(this, R.style.ButtonText);
    	t4.setBackgroundResource(R.drawable.grey_button);
        t4.setText(R.string.cancel);
        LinearLayout.LayoutParams paramst4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
        paramst4.setMargins(10, 10, 10, 10);
        t4.setLayoutParams(paramst4);
        t4.setOnClickListener(new View.OnClickListener() {  //Add a listener for when the button is pressed
            @Override
            public void onClick(View v) {
            	Intent returnIntent = new Intent();
            	setResult(RESULT_CANCELED, returnIntent);        
            	finish();
            	
            }
    });
        mainLayout.addView(t4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_identity_alert, menu);
        return true;
    }

    
    public void back(View v)
    {
    	Intent returnIntent = new Intent();
    	setResult(RESULT_CANCELED, returnIntent);        
    	finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    public void scanDiscountInterface()
    {
    	Intent discount = new Intent(this, Discount.class);
		Bundle b = new Bundle();
		b.putInt("validationCode", validationCode); 
		b.putString("idVoucher",idVoucher);
		b.putString("emisor", emisor);
		b.putString("caducity", caducity);
		b.putString("value", value);
		b.putString("description", description);
		b.putString("pin", pin);
		discount.putExtras(b);
		startActivity(discount);
		finish();
    }
    

}
