package com.beevou.android.scanner.scan;


import libraries.BeevouFunctions;
import org.json.JSONException;
import org.json.JSONObject;

import com.beevou.android.scanner.R;
import com.beevou.android.scanner.Login;
import com.beevou.android.scanner.taskmanager.AsyncTaskManager;
import com.beevou.android.scanner.taskmanager.OnTaskCompleteListener;
import com.beevou.android.scanner.taskmanager.Task;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.Html;

public class Discount extends Activity implements 
OnTaskCompleteListener {

	private String emisor;
	private String caducity;
	private String value;
	private String currency;
	private String description;
	private String idVoucher;
	private String name;
	private String requirements;
	private String comments;
	private String pinCode;
	private static String VALIDATION_CODE = "validationCode";
	private AsyncTaskManager mAsyncTaskManager;
	private Task mTask;
	private int voucher_type;
	private int cumulative_value;
	private int cumulative_target;
	
	BeevouFunctions userFunctions;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_discount);
        
       
        
        
        Bundle b = getIntent().getExtras();
        emisor = b.getString("emisor");
		caducity = b.getString("caducity");
		value =	b.getString("value");
		currency = b.getString("currency");
		description = b.getString("description");
		idVoucher =  b.getString("idVoucher");
		name =  b.getString("name");
		requirements =  b.getString("requirements");
		comments =  b.getString("comments");
		pinCode = b.getString("pin");
		voucher_type = b.getInt("voucher_type");
		cumulative_value = b.getInt("cumulative_value");
		cumulative_target = b.getInt("cumulative_target");
        
        	
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        
        LinearLayout LY1 = new LinearLayout(this);
    	LY1.setBackgroundResource(R.drawable.shape_success_cut_dot_border);//.shape_silver_no_border);
    	LinearLayout.LayoutParams params02 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
        params02.setMargins(10, 10, 10, 10);
        LY1.setOrientation(LinearLayout.VERTICAL);
        LY1.setLayoutParams(params02);
        mainLayout.addView(LY1);
        
        TextView  TVname = new TextView(this, null, android.R.attr.textAppearanceLarge);
        TVname.setText(name);
        TVname.setTextColor(Color.BLACK);
        TVname.setPadding(10, 0, 10, 5);
        LY1.addView(TVname);
        	
        if (!value.equals("null") &&! value.equals(""))
        {
        	TextView  TV3 = new TextView(this);
        	TV3.setText(value+" "+currency);
        	TV3.setTextSize(60);
        	TV3.setGravity(Gravity.CENTER_HORIZONTAL);
        	TV3.setTextColor(Color.BLACK);
        	LY1.addView(TV3);
        }
        
    	TextView  TV0 = new TextView(this, null, android.R.attr.textAppearanceSmall);
    	TV0.setText(String.format(getString(R.string.issued_by_s),emisor));
    	TV0.setTextColor(Color.BLACK);
    	TV0.setPadding(10, 5, 10, 0);
    	LY1.addView(TV0);

        if (voucher_type == 1) //0 es one use only  - 2 es multiple  
    	{
    		//This is a cumulative voucher show the status of the cumulative voucher
    		//cumulative_value + 1 this what we just read (after discount)
    		//cumulative_target = this is the target
    		
    		   		
    		TextView  TVCumulative = new TextView(this, null, android.R.attr.textAppearanceLarge);
    		TVCumulative.setText(String.format("Cumulative %s of %s", String.valueOf(cumulative_value +1) ,String.valueOf(cumulative_target)));
    		TVCumulative.setTextColor(Color.BLACK);
    		TVCumulative.setPadding(10, 0, 10, 5);

    		float uses = Float.valueOf(cumulative_value) +1;
    		float target = Float.valueOf(cumulative_target);
    		int usesLeft = cumulative_target - (cumulative_value +1);
    		
    		
    		
    		float percentCompleted = ((uses/target)*100);
    		
    		
    	  		if (percentCompleted == 0)
	  			{
    	  			TVCumulative.setBackgroundResource(R.drawable.days_left_gray);
    	  			TVCumulative.setText(String.format(getString(R.string.not_used_yet)) );
	  			} else {
	  				
	  				if (cumulative_value +1 == 1)
	  				{
	  					TVCumulative.setText(String.format(getString(R.string._1_seal_of_s_will_left),cumulative_target) );
	  				} else {
	  					TVCumulative.setText(String.format("%s "+getString(R.string.seals_of_s_will_left),usesLeft,cumulative_target) );	
	  				}
	  			}
    	  	if (percentCompleted > 0 && percentCompleted <= 25)
    	  		TVCumulative.setBackgroundResource(R.drawable.days_left_blue);
    	  	if (percentCompleted > 25 && percentCompleted <= 50)
    	  		TVCumulative.setBackgroundResource(R.drawable.days_left_green);
    	  	if (percentCompleted > 50 && percentCompleted <= 75)
    	  		TVCumulative.setBackgroundResource(R.drawable.days_left_orange);
    	  	if (percentCompleted > 75 && percentCompleted <= 100)
    	  		TVCumulative.setBackgroundResource(R.drawable.days_left_red);
            LY1.addView(TVCumulative);
    		
    	}
        
        

        	if (!caducity.equals("null") &&! caducity.equals(""))
        	{
        		TextView  TV04 = new TextView(this, null, android.R.attr.textAppearanceSmall);
        		TV04.setText(R.string.expires_on);
        		TV04.setTextColor(Color.BLACK);
        		TV04.setPadding(10, 0, 10, 0);
        		LY1.addView(TV04);

        		TextView  TV4 = new TextView(this, null, android.R.attr.textAppearanceLarge);
        		TV4.setText(caducity);
        		TV4.setTextColor(Color.BLACK);
        		TV4.setPadding(10, 0, 10, 5);
        		LY1.addView(TV4);
        	}

        	if (!description.equals("null") &&! description.equals(""))
        	{
        		TextView  TV2 = new TextView(this, null, android.R.attr.textAppearanceSmall);
        		TV2.setText(description);
        		TV2.setTextColor(Color.BLACK);
        		TV2.setPadding(10, 0, 10, 5);
        		LY1.addView(TV2);
        	}
        	
        	if (requirements != null)
        		if (!requirements.equals("null") &&! requirements.equals(""))
        		{
        			TextView  TV5 = new TextView(this,null, android.R.attr.textAppearanceLarge);
        			TV5.setText(R.string.conditions_and_requirements_);
        			TV5.setTextColor(Color.BLACK);
        			mainLayout.addView(TV5);

        			TextView  TV6 = new TextView(this);
        			TV6.setText(Html.fromHtml(requirements));
        			TV6.setTextColor(Color.BLACK);
        			mainLayout.addView(TV6);
        		}
        	
        	
        	
        	
        	LinearLayout LY2 = new LinearLayout(this);
	    	LinearLayout.LayoutParams params01 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
	        params01.setMargins(10, 10, 10, 10);
	        LY2.setOrientation(LinearLayout.VERTICAL);
	        LY2.setLayoutParams(params02);
	        mainLayout.addView(LY2);
        	
        	Button t3 = new Button(this);
            t3.setTextAppearance(this, R.style.ButtonText);
        	
        	if (voucher_type == 1)  
        	{
        		if (cumulative_value +1 == cumulative_target)
        		{
        			t3.setText("Redeem");
            		t3.setBackgroundResource(R.drawable.green_button);
        			
        		} else {
        			t3.setText("Set 1 Seal");
            		t3.setBackgroundResource(R.drawable.yellow_button);
        		}
        		
        	}
        	if (voucher_type == 2) 
        	{
        		if (cumulative_value +1 == cumulative_target)
        		{
        			t3.setText("Redeem");
            		t3.setBackgroundResource(R.drawable.green_button);
        			
        		} else {
        			t3.setText("Set 1 Use");
            		t3.setBackgroundResource(R.drawable.yellow_button);
        		}
        		
        	}
        	if (voucher_type == 0) 
        	{
        		t3.setText("Redeem");
        		t3.setBackgroundResource(R.drawable.green_button);
        	}
            t3.setOnClickListener(new View.OnClickListener() {  //Add a listener for when the button is pressed
                @Override
                public void onClick(View v) {
                	discount();
                	
                }
            });
            LY2.addView(t3);
            
        	
        }
        

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_discount, menu);
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
    
    
    public void back(View v)
    {
    	finish();
    }
    

    
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Discount.this.CONNECTIVITY_SERVICE);
        if (cm == null)
          return false;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
          return false;
        return ni.isConnectedOrConnecting();
      } 
    
    
    private void discount()
    {
    	if (isOnline()) {    	
    		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
    			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    		} else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    		mAsyncTaskManager = new AsyncTaskManager(this, this, getString(R.string.discounting_voucher_));
    		mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());
    		mTask = new  discountVoucher(getResources());
    		mAsyncTaskManager.setupTask(mTask);
    	} else {
    		Toast.makeText(Discount.this, R.string.no_network_connection, Toast.LENGTH_LONG).show();
    	}
    }

    private class discountVoucher extends Task {



		public discountVoucher(Resources resources) {
			super(resources);
			
		}

		private JSONObject json;
        
        
        
        @Override
        protected Boolean doInBackground(Void... arg0) {
        	json = BeevouFunctions.getInstance().discountQR(idVoucher,pinCode);
		return true;
		}
		
        public JSONObject getResult() {
            return json;
        }
		
		
		
    }


	@Override
	public void onTaskComplete(Task task) {
		if (!task.isCancelled()) {
    		JSONObject json = ((discountVoucher) mTask).getResult();
    		processResult(json);
		}
	}
    
    
    
	private void processResult(JSONObject json)
	{
		try {
			if (json.has("AuthError") == true) {
				if (json.getString("AuthError").equals("invalid_grant"))
				{	
					Toast.makeText(Discount.this, R.string.incorrect_username_password, Toast.LENGTH_LONG).show(); 

					Intent login = new Intent(getApplicationContext(), Login.class);
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Bundle b = new Bundle();
					b.putString("GCM_regID", "");
					login.putExtras(b);
					startActivity(login);
					finish();
				} else {
					Toast.makeText(Discount.this, json.getString("AuthError"), Toast.LENGTH_LONG).show();
				}
			} else {
			if (json.getString(VALIDATION_CODE) != null) {
    			
    			
    			
    			if (Integer.parseInt(json.getString(VALIDATION_CODE)) == 1)
    			{
    				final AlertDialog.Builder alertaSimple = new AlertDialog.Builder(Discount.this);
    				alertaSimple.setTitle(R.string.message);
    				alertaSimple.setMessage(R.string.the_voucher_has_been_discounted_succesfully);
    				alertaSimple.setPositiveButton("Ok",
    						new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog, int which) {
    						finish();
    					}
    				});
    				alertaSimple.show();
    			} else {
    				
    				
    				final AlertDialog.Builder alertaSimple = new AlertDialog.Builder(Discount.this);
    				alertaSimple.setTitle(R.string.message);
    				alertaSimple.setMessage(R.string.there_has_been_an_error_trying_to_discount_the_voucher_please_contact_beevou_for_more_information);
    				alertaSimple.setPositiveButton("Ok",
    						new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog, int which) {
    						finish();
    					}
    				});
    				alertaSimple.show();
    			}
    		}
			}	
    	} catch (JSONException e) {
    	    e.printStackTrace();
    	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    	}
		

	}
	
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	if (mAsyncTaskManager != null)
    		if (mAsyncTaskManager.isWorking())
    		{
    		mTask.cancel(true);
    		}
    }
  

}
