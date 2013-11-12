package com.beevou.android.scanner.scan;



import org.json.JSONException;
import org.json.JSONObject;

import libraries.BeevouFunctions;


import com.beevou.android.scanner.Login;
import com.beevou.android.scanner.taskmanager.AsyncTaskManager;
import com.beevou.android.scanner.taskmanager.OnTaskCompleteListener;
import com.beevou.android.scanner.taskmanager.Task;
import com.beevou.android.scanner.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;

public class ScannedVoucher extends Activity  implements 
OnTaskCompleteListener {
	

	private String value;
	private String description;
	private int status;
	private String discounted_on;
	private String discounted_on_id;
	private String discount_date;
	private String idvoucher;
	private String currency;
	private String stringValue;
	private int numberDaysLeft;
	private Context context = this;
	private String voucher_template_type;
	private String voucher_template_name;
	private String emisor;
	private String voucherReadID;
	private String affiliateValue;
	private String affiliateCurrency;
	private String affiliateStringValue;
	
	private AsyncTaskManager mAsyncTaskManager;
	private Task mTask;
	
	
	
	//private BaseVoucher voucher;
	
	private LinearLayout LY01;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scanned_voucher);
        
        Bundle b = getIntent().getExtras();
		value =	b.getString("value");
		currency = b.getString("currency");
		stringValue = b.getString("stringValue");
		affiliateValue = b.getString("affiliate_value");
		affiliateCurrency = b.getString("affiliate_currency");
		affiliateStringValue = b.getString("stringAffiliateValue");
		description = b.getString("description");
		status = b.getInt("status");
		discounted_on = b.getString("discount_on");
		discounted_on_id = b.getString("discount_on_id");
		discount_date = b.getString("discount_date");
		idvoucher = b.getString("idvoucher");
		emisor = b.getString("emisor"); 
		String issuerID = b.getString("issuerID");
		voucherReadID = b.getString("voucherReadID");
		String cumulativeStatus = b.getString("cumulativeStatus");
		String voucherStatus = b.getString("voucherStatus");
		String cumulativeStatusString = b.getString("cumulativeStatusString");
		String voucherType = b.getString("voucherType");
		
		   
		
		voucher_template_type = b.getString("voucher_template_type");

		voucher_template_name = b.getString("voucher_template_name");
		
		TextView voucherName = (TextView) findViewById(R.id.voucherName);
		voucherName.setText(voucher_template_name);
		
		TextView voucherID = (TextView) findViewById(R.id.voucherID);
		voucherID.setText("ID: "+idvoucher);
		
		TextView cumulativeStatusBox = (TextView) findViewById(R.id.cumulativeStatus);
		if (voucherType.equals("0"))
		{
			cumulativeStatusBox.setVisibility(View.INVISIBLE);
		} else {
			cumulativeStatusBox.setText(cumulativeStatusString);
		}
		
		
		
		TextView discountDate = (TextView) findViewById(R.id.discountDate);
		discountDate.setText(String.format(getString(R.string.discounted_on_s) ,discount_date));
		
		TextView issuerName = (TextView) findViewById(R.id.issuerName);
		String issuedByLink = String.format("<a href='https://beevou.net/affiliates/view/%s'> %s </a>",issuerID,emisor);
		issuerName.setText(Html.fromHtml(String.format(getString(R.string.issued_by_s),issuedByLink)));
		issuerName.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView value = (TextView) findViewById(R.id.value);
		LinearLayout valueLayout = (LinearLayout) findViewById(R.id.nominalValueLayout);
		if (stringValue != null &&! stringValue.equals("") &&! stringValue.equals("null") )
        {
			value.setText(stringValue);
        } else {
        	valueLayout.setVisibility(View.GONE);
        }
		
		TextView affValue = (TextView) findViewById(R.id.affiliateValue);
		LinearLayout affValueLayout = (LinearLayout) findViewById(R.id.affiliateValueLayout);
		if (affiliateStringValue != null &&! affiliateStringValue.equals("") &&! affiliateStringValue.equals("null") )
        {
			affValue.setText(affiliateStringValue);
        } else {
        	affValueLayout.setVisibility(View.GONE);
        }
		
		if (description != null)
    		if (!description.equals("null") &&! description.equals(""))
    		{
    			TextView voucherDescription = (TextView) findViewById(R.id.voucherDescription);
    			voucherDescription.setText(description);
    		} else {
    			
    		}
		
    	
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_voucher, menu);
        return true;
    }

    public void back(View v)
    {
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
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }
    

    
    public void backToHolder(View v)
    {
    	if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
    	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	} else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setMessage(R.string.are_you_sure_you_want_to_back_this_voucher_to_the_holder_)
    	.setCancelable(true)
    	.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int id) {
    			dialog.cancel();
    			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    			
    		}
    	})
    	.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int id) {
    			back();  
    		}
    	});
    	AlertDialog alert = builder.create();
    	alert.show();	
    	
    } 

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(ScannedVoucher.this.CONNECTIVITY_SERVICE);
        if (cm == null)
          return false;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
          return false;
        return ni.isConnectedOrConnecting();
      } 
    
    private void back()
    {
    	if (isOnline()) {	
    		mAsyncTaskManager = new AsyncTaskManager(this, this, getString(R.string.turning_the_voucher_back_to_the_beneficiary_));
    		mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());
    		mTask = new  BackToHolder(getResources());
    		mAsyncTaskManager.setupTask(mTask);

    	} else {
    		Toast.makeText(ScannedVoucher.this, R.string.no_network_connection, Toast.LENGTH_LONG).show();
    	}
    }

    private class BackToHolder extends Task {



		public BackToHolder(Resources resources) {
			super(resources);
			
		}

		private JSONObject json;
        
        
        
        @Override
        protected Boolean doInBackground(Void... arg0) {
        	json = BeevouFunctions.getInstance().backToHolder(voucherReadID);
		return true;
		}
		
        public JSONObject getResult() {
            return json;
        }
		
		
		
    }


	@Override
	public void onTaskComplete(Task task) {
		if (!task.isCancelled()) {
    		JSONObject json = ((BackToHolder) mTask).getResult();
    		processResult(json);
		}
	}
    
    
    
	private void processResult(JSONObject json)
	{
		try {
			if (json.has("AuthError") == true) {
				if (json.getString("AuthError").equals("invalid_grant"))
				{	
					Toast.makeText(ScannedVoucher.this, R.string.incorrect_username_password, Toast.LENGTH_LONG).show(); 

					Intent login = new Intent(getApplicationContext(), Login.class);
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Bundle b = new Bundle();
					b.putString("GCM_regID", "");
					login.putExtras(b);
					startActivity(login);
					finish();
				} else {
					Toast.makeText(ScannedVoucher.this, json.getString("AuthError"), Toast.LENGTH_LONG).show();
				}
			} else {
			if (json.getString("validationCode") != null) {
				if (Integer.parseInt(json.getString("validationCode")) == 1)
				{
					final AlertDialog.Builder alertaSimple = new AlertDialog.Builder(context);
					alertaSimple.setTitle(R.string.message);
					alertaSimple.setMessage(R.string.the_voucher_has_been_back_to_the_holder_succesfully);
					alertaSimple.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent returnIntent = new Intent();
							setResult(RESULT_OK, returnIntent);        
							finish();
						}
					});
					alertaSimple.show();

				} else {
					//TODO change this for a generic activity to offer the message and give more info and next steps for the user

					final AlertDialog.Builder alertaSimple = new AlertDialog.Builder(context);
					alertaSimple.setTitle(R.string.message);
					alertaSimple.setMessage(R.string.there_has_been_an_error_trying_to_back_the_voucher_to_the_holder_please_contact_beevou_for_more_information);
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


