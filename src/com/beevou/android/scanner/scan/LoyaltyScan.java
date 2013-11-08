package com.beevou.android.scanner.scan;

import libraries.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beevou.android.scanner.Login;
import com.beevou.android.scanner.R;
import com.beevou.android.scanner.taskmanager.AsyncTaskManager;
import com.beevou.android.scanner.taskmanager.OnTaskCompleteListener;
import com.beevou.android.scanner.taskmanager.Task;

public class LoyaltyScan extends Activity implements 
OnTaskCompleteListener {
	
	
	private String emisor;
	private String caducity;
	private String description;
	private String holder;
	private Integer validationCode;
	private String requirements;
	private String comments;
	private Integer voucher_type;
	private String name;
	private String idVoucher;
	private Integer loyalty_points_accounting;
	private Integer	loyalty_points_per_scan;
	private Float loyalty_money_amount;
	private Integer loyalty_points_per_currency_unit;
	private Integer points;
	private LinearLayout LY2;
	private LinearLayout mainLayout;
	private static String VALIDATION_CODE = "validationCode";
	private Task mTask;
	private AsyncTaskManager mAsyncTaskManager;
	private String value_to_send;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_loyalty);
	        
	       
	        
	        
	        Bundle b = getIntent().getExtras();
	        emisor = b.getString("emisor");
			caducity = b.getString("caducity");
			description = b.getString("description");
			idVoucher =  b.getString("idVoucher");
			name =  b.getString("name");
			requirements =  b.getString("requirements");
			comments =  b.getString("comments");
			voucher_type = b.getInt("voucher_type");
			loyalty_points_accounting = b.getInt("loyalty_points_accounting");
			loyalty_points_per_scan = b.getInt("loyalty_points_per_scan");
			loyalty_money_amount = b.getFloat("loyalty_money_amount");
			loyalty_points_per_currency_unit = b.getInt("loyalty_points_per_currency_unit");
			points = b.getInt("points");
			
			
			
			
			mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
	        
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
	        	
	        if (!points.equals("null") &&! points.equals(""))
	        {
	        	TextView  TV3 = new TextView(this);
	        	TV3.setText(getString(R.string.accumulated_loyalty_points_balance) +": "+points);
	        	TV3.setTextSize(24);
	        	//TV3.setGravity(Gravity.CENTER_HORIZONTAL);
	        	TV3.setTextColor(Color.BLACK);
	        	LY1.addView(TV3);
	        }
	        
	    	TextView  TV0 = new TextView(this, null, android.R.attr.textAppearanceSmall);
	    	TV0.setText(String.format(getString(R.string.issued_by_s),emisor));
	    	TV0.setTextColor(Color.BLACK);
	    	TV0.setPadding(10, 5, 10, 0);
	    	LY1.addView(TV0);


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
	        		TV2.setPadding(10, 10, 10, 5);
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
	        	
	        	
	        	
	        	
	        	LY2 = new LinearLayout(this);
		    	LinearLayout.LayoutParams params01 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
		        params01.setMargins(10, 10, 10, 10);
		        LY2.setOrientation(LinearLayout.VERTICAL);
		        LY2.setLayoutParams(params01);
		        mainLayout.addView(LY2);
		        
		        
		        
		        LinearLayout LY21 = new LinearLayout(this);
		    	LinearLayout.LayoutParams params011 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
		        params011.setMargins(10, 10, 10, 10);
		        LY21.setOrientation(LinearLayout.VERTICAL);
		        LY21.setLayoutParams(params011);
		        LY2.addView(LY21);
	        	
			        	Button t3 = new Button(this);
			        	t3.setPadding(5, 5, 5, 5);
			            t3.setTextAppearance(this, R.style.ButtonText);
			        	t3.setText(R.string.add_reward_points);
		            	t3.setBackgroundResource(R.drawable.yellow_button);
		            	t3.setOnClickListener(new View.OnClickListener() {  //Add a listener for when the button is pressed
			                @Override
			                public void onClick(View v) {
			                	addRewardPoints();
			                }
			            });
		            	LY21.addView(t3);
	        	

		        LinearLayout LY22 = new LinearLayout(this);
		    	LinearLayout.LayoutParams params012 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
		    	params012.setMargins(10, 10, 10, 10);
		        LY22.setOrientation(LinearLayout.VERTICAL);
		        LY22.setLayoutParams(params012);
		        LY2.addView(LY22);
			        	    	
		            	
		            	
		            	Button t4 = new Button(this);
		            	t4.setPadding(5, 5, 5, 5);
			            t4.setTextAppearance(this, R.style.ButtonText);
			        	t4.setText(R.string.redeem_points);
			            t4.setBackgroundResource(R.drawable.green_button);
			        			
			            t4.setOnClickListener(new View.OnClickListener() {  //Add a listener for when the button is pressed
			                @Override
			                public void onClick(View v) {
			                	//discount();
			                	
			                }
			            });
			            
			            LY22.addView(t4);
			
			
	 }
	
	private void addRewardPoints()
	{
		mainLayout.removeView(LY2);
		
		LinearLayout LY3 = new LinearLayout(this);
    	LY3.setBackgroundResource(R.drawable.corporate_shape);
		LinearLayout.LayoutParams params01 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
        params01.setMargins(10, 10, 10, 10);
        LY3.setOrientation(LinearLayout.VERTICAL);
        LY3.setLayoutParams(params01);
        mainLayout.addView(LY3);
        
        /*
		 * 
			'0' => __('Add the same points amount on each scan')
			'1' => __('Add points according to a money ratio')
			'2' => __('Manual points intro')
		 * 
		 */
        
        if (loyalty_points_accounting == 0)
        {
        	//Direct to the addpoints the server will set the ammount to the points balance
        }
        
        if (loyalty_points_accounting == 1)
        {
        	//The user has to insert the money value of the transaction
        	LinearLayout et2 = new LinearLayout(this);
	    	LinearLayout.LayoutParams paramset2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
	        paramset2.setMargins(10, 10, 10, 10);
	        et2.setOrientation(LinearLayout.VERTICAL);
	        et2.setLayoutParams(paramset2);
	        LY3.addView(et2);
        	
        	
		        	TextView  TV2 = new TextView(this);
		        	TV2.setPadding(10, 10, 10, 10);
		        	TV2.setEms(24);
					TV2.setText(R.string.please_introduce_here_the_amount_of_the_transaction);
					TV2.setTextColor(Color.parseColor("#ffffff"));
					et2.addView(TV2);
			
			
		
			LinearLayout et1 = new LinearLayout(this);
			LinearLayout.LayoutParams paramset1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
	        paramset1.setMargins(10, 10, 10, 10);
	        et1.setOrientation(LinearLayout.VERTICAL);
	        et1.setLayoutParams(paramset1);
	        LY3.addView(et1);
			
					EditText editPin = new EditText(this);
					editPin.setBackgroundColor(Color.WHITE);
					editPin.setEms(24);
					editPin.setInputType(InputType.TYPE_CLASS_NUMBER);
					editPin.setHint(R.string.transaction_amount);
					editPin.setTextColor(Color.BLACK);
					et1.addView(editPin);
					
			LinearLayout et3 = new LinearLayout(this);
	    	LinearLayout.LayoutParams paramset3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
	        paramset3.setMargins(10, 10, 10, 10);
	        et3.setOrientation(LinearLayout.VERTICAL);
	        et3.setLayoutParams(paramset3);
	        LY3.addView(et3);		
			
					Button t5 = new Button(this);
					t5.setPadding(10, 10, 10, 10);
		            t5.setTextAppearance(this, R.style.ButtonText);
		        	t5.setText(R.string.process);
		            t5.setBackgroundResource(R.drawable.green_button);
		        			
		            t5.setOnClickListener(new View.OnClickListener() {  //Add a listener for when the button is pressed
		                @Override
		                public void onClick(View v) {
		                	//discount();
		                	
		                }
		            });
		            et3.addView(t5);
        }
        
        
        if (loyalty_points_accounting == 2)
        {
        	//The user has to insert the money value of the transaction
        	LinearLayout et2 = new LinearLayout(this);
	    	LinearLayout.LayoutParams paramset2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
	        paramset2.setMargins(10, 10, 10, 10);
	        et2.setOrientation(LinearLayout.VERTICAL);
	        et2.setLayoutParams(paramset2);
	        LY3.addView(et2);
        	
        	
		        	TextView  TV2 = new TextView(this);
		        	TV2.setPadding(10, 10, 10, 10);
		        	TV2.setEms(16);
					TV2.setText(R.string.please_introduce_here_the_amount_of_points_to_be_added);
					TV2.setTextColor(Color.parseColor("#FFFFFF"));
					et2.addView(TV2);
			
			
		
			LinearLayout et1 = new LinearLayout(this);
	    	//LY3.setBackground(R.drawable.androidform_13);
			LinearLayout.LayoutParams paramset1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
	        paramset1.setMargins(10, 10, 10, 10);
	        et1.setOrientation(LinearLayout.VERTICAL);
	        et1.setLayoutParams(paramset1);
	        LY3.addView(et1);
			
					EditText editPin = new EditText(this);
					editPin.setBackgroundColor(Color.WHITE);
					editPin.setEms(24);
					editPin.setInputType(InputType.TYPE_CLASS_NUMBER);
					editPin.setHint(R.string.points_amount);
					editPin.setTextColor(Color.BLACK);
					et1.addView(editPin);
					
			LinearLayout et3 = new LinearLayout(this);
	    	//LY3.setBackground(R.drawable.androidform_13);
			LinearLayout.LayoutParams paramset3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
	        paramset3.setMargins(10, 10, 10, 10);
	        et3.setOrientation(LinearLayout.VERTICAL);
	        et3.setLayoutParams(paramset3);
	        LY3.addView(et3);		
			
					Button t5 = new Button(this);
					t5.setPadding(10, 10, 10, 10);
		            t5.setTextAppearance(this, R.style.ButtonText);
		        	t5.setText(R.string.process);
		            t5.setBackgroundResource(R.drawable.green_button);
		        			
		            t5.setOnClickListener(new View.OnClickListener() {  //Add a listener for when the button is pressed
		                @Override
		                public void onClick(View v) {
		                	//discount();
		                	
		                }
		            });
		            et3.addView(t5);
        }
	}
		

	
	
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(LoyaltyScan.this.CONNECTIVITY_SERVICE);
        if (cm == null)
          return false;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
          return false;
        return ni.isConnectedOrConnecting();
      } 
	
	private void process()
    {
    	if (isOnline()) {    	
    		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
    			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    		} else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    		mAsyncTaskManager = new AsyncTaskManager(this, this, getString(R.string.discounting_voucher_));
    		mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());
    		mTask = new  processVoucher(getResources());
    		mAsyncTaskManager.setupTask(mTask);
    	} else {
    		Toast.makeText(LoyaltyScan.this, R.string.no_network_connection, Toast.LENGTH_LONG).show();
    	}
    }
	
	
	
	private class processVoucher extends Task {



		public processVoucher(Resources resources) {
			super(resources);
			// TODO Auto-generated constructor stub
		}

		private JSONObject json;
        
        
        
        @Override
        protected Boolean doInBackground(Void... arg0) {
        	json = UserFunctions.getInstance().addRewards(idVoucher,value_to_send);
		return true;
		}
		
        public JSONObject getResult() {
            return json;
        }
		
		
		
    }


	@Override
	public void onTaskComplete(Task task) {
		if (!task.isCancelled()) {
    		JSONObject json = ((processVoucher) mTask).getResult();
    		processResult(json);
		}
	}
    
    
    
	private void processResult(JSONObject json)
	{
		try {
			if (json.has("AuthError") == true) {
				if (json.getString("AuthError").equals("invalid_grant"))
				{	
					Toast.makeText(LoyaltyScan.this, R.string.incorrect_username_password, Toast.LENGTH_LONG).show(); 

					Intent login = new Intent(getApplicationContext(), Login.class);
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Bundle b = new Bundle();
					b.putString("GCM_regID", "");
					login.putExtras(b);
					startActivity(login);
					finish();
				} else {
					Toast.makeText(LoyaltyScan.this, json.getString("AuthError"), Toast.LENGTH_LONG).show();
				}
			} else {
			if (json.getString(VALIDATION_CODE) != null) {
    			
    			
    			
    			if (Integer.parseInt(json.getString(VALIDATION_CODE)) == 1)
    			{
    				final AlertDialog.Builder alertaSimple = new AlertDialog.Builder(LoyaltyScan.this);
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
    				
    				
    				final AlertDialog.Builder alertaSimple = new AlertDialog.Builder(LoyaltyScan.this);
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
	
	

	
	

	
    public void back(View v)
    {
    	finish();
    }

}
