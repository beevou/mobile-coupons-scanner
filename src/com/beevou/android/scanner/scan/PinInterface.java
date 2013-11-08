package com.beevou.android.scanner.scan;

import libraries.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import com.beevou.android.scanner.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;

public class PinInterface extends Activity {
	
	private String emisor;
	private String caducity;
	private String value;
	private String description;
	private String holder;
	private String pin;
	private Integer validationCode;
	private String idVoucher;
	private LinearLayout mainLayout;
	private EditText editPin;
	//private ImageView pinImage;
	private TextView  pinCodeMessage;
	private Button t3;
	private LinearLayout LY2;
	private LinearLayout LY1;
	private Context mcontext = this;
	
	
	UserFunctions userFunctions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pin_interface);


        
        Bundle b = getIntent().getExtras();
        	validationCode  = b.getInt("validationCode");
        	idVoucher =  b.getString("idVoucher"); 
        	emisor = b.getString("emisor");
			caducity = b.getString("caducity");
			value =	b.getString("value");
			description = b.getString("description");
			holder = b.getString("holder");
			pin = b.getString("pin");
			//attempts = b.getInt("attempts");
        
			mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
			
			



				

			LY1 = new LinearLayout(this);
	    	LY1.setBackgroundResource(R.drawable.shape_warning_dot_border);
	    	LinearLayout.LayoutParams params02 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
	        params02.setMargins(10, 10, 10, 10);
	        LY1.setOrientation(LinearLayout.VERTICAL);
	        LY1.setLayoutParams(params02);
	        mainLayout.addView(LY1);

				
				TextView  TV = new TextView(this, null, android.R.attr.textAppearanceLarge);
				TV.setText(R.string.this_voucher_has_a_security_pin_associated_);
				TV.setTextColor(Color.parseColor("#C09853"));
				LY1.addView(TV);
				TextView  TV2 = new TextView(this);
				TV2.setText(R.string.please_ask_the_beneficiary_to_introduce_the_identification_pin_for_this_voucher_);
				TV2.setTextColor(Color.parseColor("#C09853"));
				LY1.addView(TV2);
				pinCodeMessage = new TextView(this, null, android.R.attr.textAppearanceMedium);

				
				
				
				
			
			editPin = new EditText(this);
			editPin.setInputType(InputType.TYPE_CLASS_NUMBER);
			editPin.setHint("Pin Code");
			editPin.setTextColor(Color.BLACK);
			editPin.setTransformationMethod(PasswordTransformationMethod.getInstance());
			LY1.addView(editPin);
			
			LY2 = new LinearLayout(this);
	    	LinearLayout.LayoutParams params01 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
	        params01.setMargins(10, 10, 10, 10);
	        LY2.setOrientation(LinearLayout.VERTICAL);
	        LY2.setLayoutParams(params02);
	        mainLayout.addView(LY2);

			t3 = new Button(this);
			t3.setTextAppearance(this, R.style.ButtonText);
			t3.setText("validate");
			t3.setBackgroundResource(R.drawable.blue_button);
			LY2.addView(t3);

			t3.setOnClickListener(new View.OnClickListener() {  
				@Override
				public void onClick(View v) {
					String introduced = editPin.getText().toString();
					if (!introduced.equals(""))
					{
						
						validatePin();
					} else {
							//Alert must introduce a pin code
						alertMessage(getString(R.string.must_introduce_a_pin_code_for_the_voucher_));
						
						
					}
				}
			});
			
			
			pinCodeMessage.setTextColor(Color.RED);
			LY1.addView(pinCodeMessage);
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pin_interface, menu);
        return true;
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
		b.putString("holder", holder);
		b.putString("pin", editPin.getText().toString());
		discount.putExtras(b); 
		startActivity(discount);
		finish();
    }
    
    private void scanPinInterface()
    {
    	Intent pinIterface = new Intent(this, PinInterface.class);
		Bundle b = new Bundle();
		b.putInt("validationCode", validationCode); 
		b.putString("idVoucher",idVoucher);
		b.putString("emisor", emisor);
		b.putString("caducity", caducity);
		b.putString("value", value);
		b.putString("description", description);
		b.putString("holder", holder);
		b.putString("pin", pin);
		pinIterface.putExtras(b); 
		startActivity(pinIterface);
		finish();
    }
    
    public void alertMessage(String message)
    {
 	   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

 			// set title
 			alertDialogBuilder.setTitle("");

 			// set dialog message
 			alertDialogBuilder
 				.setMessage(message)
 				.setCancelable(false)
 				.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
 					public void onClick(DialogInterface dialog,int id) {

 					}
 				  })
 				.setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
 					public void onClick(DialogInterface dialog,int id) {
 						dialog.cancel();
 					}
 				});

 				// create alert dialog
 				AlertDialog alertDialog = alertDialogBuilder.create();

 				// show it
 				alertDialog.show();
    }
    
    private void blockVoucherByPinViolation()
    {

    				ImageView IV = new ImageView(this);
    		    	IV.setImageResource(R.drawable.error_fatal_128);
    		    	mainLayout.addView(IV);
    		    	
    		    	TextView  TV = new TextView(this, null, android.R.attr.textAppearanceLarge);
    		    	TV.setText(R.string.this_voucher_has_been_locked_due_to_pin_restrictions_violation_);
    		    	TV.setTextColor(Color.RED);
    		    	TV.setPadding(20, 5, 20, 5);
    		    	mainLayout.addView(TV);
    		    	
    		    	TextView  TV2 = new TextView(this);
    				TV2.setText(R.string.the_beneficiary_of_the_voucher_can_unlock_it_in_his_her_private_area_);
    				TV2.setTextColor(Color.BLACK);
    				TV2.setPadding(20, 5, 20, 5);
    				mainLayout.addView(TV2);
    				
    				Button t4 = new Button(this);
    				t4.setText(R.string.cancel);
    				t4.setOnClickListener(new View.OnClickListener() {  //Add a listener for when the button is pressed
    					@Override
    					public void onClick(View v) {
    						finish();

    					}
    				});
    				mainLayout.addView(t4);
    				
    }
    
    private void validatePin()
    {
    	new Asyn_Task().execute("",""); 
    	

    }
    
    public void back(View v)
    {
    	finish();
    }
    
    
    private class Asyn_Task extends AsyncTask<String, String, Void> {
        private final ProgressDialog dialog = new ProgressDialog(PinInterface.this);
        private JSONObject json;
        private ListView lv = (ListView) findViewById(R.id.list);
        // can use UI thread here
        protected void onPreExecute() {
        	String msg = getString(R.string.processing_);
            this.dialog.setMessage(msg);
            this.dialog.setCancelable(false);
            this.dialog.show();
        }


        @Override
        protected void onPostExecute(Void result) {
        	processResult();
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
        }
        

        
        
		@Override
		protected Void doInBackground(String... arg0) {

	    	json = UserFunctions.getInstance().checkPin(idVoucher,editPin.getText().toString());
	    	    	    
		return null;
		}
		
		
		private void processResult()
		{
	    	Boolean result = false;
	    	try
	    	{
	    		    		
	    		
	    		if (json.getString("validationCode") != null) {
	    			if (Integer.parseInt(json.getString("validationCode")) == 1)
	    			{
	    				mainLayout.removeAllViews();
						scanDiscountInterface();
	    			} else {
	    				//pin incorrect
	    				if (json.getString("error").equals(R.string.invalid_voucher))
	    				{
	    					final AlertDialog.Builder alertaSimple = new AlertDialog.Builder(PinInterface.this);
	        				alertaSimple.setTitle(R.string.invalid_voucher);
	        				alertaSimple.setMessage(json.getString("error_msg"));
	        				alertaSimple.setPositiveButton("Ok",
	        						new DialogInterface.OnClickListener() {
	        					public void onClick(DialogInterface dialog, int which) {
	        						finish();
	        					}
	        				});
	        				alertaSimple.show();
	    				} else {
	    					pinCodeMessage.setText(json.getString("error_msg"));
	    					editPin.setText("");
	    					if (json.has("attempts_left"))
	    					{
	    						Integer attempts =	Integer.parseInt(json.getString("attempts_left"));

	    						if (attempts == 0)
	    						{
	    							t3.setBackgroundResource(R.drawable.blue_button);
	    						}
	    						if (attempts == 1)
	    						{
	    							LY1.setBackgroundResource(R.drawable.shape_alert_dot_border);
	    							pinCodeMessage.setTextColor(Color.parseColor("#B94A8A"));
	    							t3.setBackgroundResource(R.drawable.red_button);
	    						}

	    						if (attempts == 2)
	    						{
	    							pinCodeMessage.setTextColor(Color.parseColor("#B94A8A"));
	    							t3.setBackgroundResource(R.drawable.yellow_button);
	    						}

	    					} 
	    					
	    					if (json.getString("errorCode").equals("1"))
	    						{
	    						t3.setVisibility(View.GONE);
	    						final AlertDialog.Builder alertaSimple2 = new AlertDialog.Builder(PinInterface.this);
	            				alertaSimple2.setTitle(R.string.voucher_locked);
	            				alertaSimple2.setMessage(json.getString("error_msg"));
	            				alertaSimple2.setPositiveButton("Ok",
	            						new DialogInterface.OnClickListener() {
	            					public void onClick(DialogInterface dialog, int which) {
	            						finish();	
	            					}
	            				});
	            				alertaSimple2.show(); 
	    						
	    						}
	    					
	    				}
	    				result = false;
	    			}
	    		}

	    	}  catch (JSONException e) {
	    		e.printStackTrace();
	    		result = false;
	    	}
		}
		
		
		
	   
		
		
    }

}
