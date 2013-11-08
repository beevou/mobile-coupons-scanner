package com.beevou.android.scanner.scan;

import libraries.UserFunctions;

import net.sourceforge.zbar.Symbol;

import org.json.JSONException;
import org.json.JSONObject;

import com.beevou.android.scanner.R;
import com.beevou.android.scanner.R.layout;
import com.beevou.android.scanner.R.menu;
import com.beevou.android.scanner.Dashboard;
import com.beevou.android.scanner.Login;
import com.beevou.android.scanner.taskmanager.AsyncTaskManager;
import com.beevou.android.scanner.taskmanager.OnTaskCompleteListener;
import com.beevou.android.scanner.taskmanager.Task;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.media.MediaPlayer.OnCompletionListener;
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
import android.content.res.Resources;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

public class ScanCamera extends Activity   implements 
OnTaskCompleteListener {
	
	private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;
    private static String VALIDATION_CODE = "validationCode";
    private static String EMISOR = "issuer_name";
    private static String VALUE = "value";
    private static String CURRENCY = "currency";
    private static String DESCRIPTION = "voucher_template_description";
    private static String CADUCITY = "end_date";
    private static String HOLDER = "holder";
    private static String PIN = "pin_code";
    private String qrCode;
    
	private AsyncTaskManager mAsyncTaskManager;
	private Task mTask;
	private Boolean pendingValidation = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_scan_camera);
		 
		
		
	initCamera();	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		return true;
	}
	
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	switch (requestCode) {
	    		case ZBAR_SCANNER_REQUEST:
	    		{
	    			//case ZBAR_QR_SCANNER_REQUEST:
	    			if (resultCode == RESULT_OK) {
	    				qrCode = data.getStringExtra(ZBarConstants.SCAN_RESULT);
	    				
	    				if (pendingValidation == false)
	    					decode();
	    				break;
	    			}
	    		}
	    		case 10:
	    		{
	    			if (resultCode == -1) {
	    				initCamera();	
	    			}
	    		}
	    	}
	    	
	    	if (resultCode == RESULT_CANCELED) {
	    		finish();
	    	}
	    	
	    	
	    	
			
	    }
	
/*	
    private class DiscountAsyn_Task extends AsyncTask<String, String, Void> {
        private final ProgressDialog dialog = new ProgressDialog(ScanCamera.this);
        private JSONObject json;

        // can use UI thread here
        protected void onPreExecute() {
            this.dialog.setMessage("Processing...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }
        
        private final OnCompletionListener beepListener = new OnCompletionListener() {
    		public void onCompletion(MediaPlayer mediaPlayer) {
    			mediaPlayer.seekTo(0);
    		}
    	};


        @Override
        protected void onPostExecute(Void result) {
        	processResult();
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
        }
        

        
        
		@Override
		protected Void doInBackground(String... arg0) {
			//toneMessage();
			//initBeepSound();
			
			ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_RING, 200);
			 tg.startTone(ToneGenerator.TONE_PROP_PROMPT);//.TONE_PROP_BEEP2);//.TONE_SUP_ERROR);//.TONE_PROP_BEEP);
	    			json = UserFunctions.getInstance().validateQR(qrCode);
		return null;
		}
		
		
		private void processResult()
		{
			try { 
		    			// check for login response
		    				if (json.getString(VALIDATION_CODE) != null) {

		    					Intent scanActivity = new Intent(ScanCamera.this, ScanActivity.class);
		    					Bundle b = new Bundle();
		    					b.putInt("validationCode", Integer.parseInt(json.getString(VALIDATION_CODE))); 
		    					if (json.has("error"))
		    						b.putString("error", json.getString("error"));
		    					else
		    						b.putString("error", "");
		    					if (json.has("error_msg"))
		    						b.putString("error_msg", json.getString("error_msg"));
		    					else
		    						b.putString("error_msg", "");
		    					
		    					//b.putString("qrCode",qrCode);
		    					if (Integer.parseInt(json.getString(VALIDATION_CODE)) == 1)
		    					{

		    						JSONObject voucher = json.getJSONObject("voucher");
		    						String s = voucher.getString("id");
		    						b.putString("idVoucher", voucher.getString("id"));
		    						b.putString("emisor", voucher.getString(EMISOR));
		    						b.putString("caducity", voucher.getString(CADUCITY));
		    						b.putString("value", voucher.getString(VALUE));
		    						b.putString("currency", voucher.getString("currency"));
		    						b.putString("description", voucher.getString(DESCRIPTION));
		    						b.putString("requirements",voucher.getString("voucher_template_instructions"));
		    						b.putString("comments",voucher.getString("voucher_template_comments"));
		    						//TODO b.putString("holder", voucher.getString(HOLDER));
		    						b.putString("holder", "");
		    						b.putString("pin", voucher.getString(PIN));
		    					}
		    					scanActivity.putExtras(b); //Put your id to your next Intent
		    					//startActivity(scanActivity);
		    					startActivityForResult(scanActivity, RESULT_OK);
		    					finish();
		    					
		    					
		    				}
		    				
		    				
		    				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		
		
    }
  */  
    
 /*   private void decode()
    {
    	
    	ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_RING, 200);
		 tg.startTone(ToneGenerator.TONE_PROP_PROMPT);
    	
    	try { 
    		
    		JSONObject json = UserFunctions.getInstance().validateQR(qrCode);
			// check for login response
				if (json.getString(VALIDATION_CODE) != null) {

					Intent scanActivity = new Intent(ScanCamera.this, ScanActivity.class);
					Bundle b = new Bundle();
					b.putInt("validationCode", Integer.parseInt(json.getString(VALIDATION_CODE))); 
					if (json.has("error"))
						b.putString("error", json.getString("error"));
					else
						b.putString("error", "");
					if (json.has("error_msg"))
						b.putString("error_msg", json.getString("error_msg"));
					else
						b.putString("error_msg", "");
					
					//b.putString("qrCode",qrCode);
					if (Integer.parseInt(json.getString(VALIDATION_CODE)) == 1)
					{

						JSONObject voucher = json.getJSONObject("voucher");
						String s = voucher.getString("id");
						b.putString("idVoucher", voucher.getString("id"));
						b.putString("emisor", voucher.getString(EMISOR));
						b.putString("caducity", voucher.getString(CADUCITY));
						b.putString("value", voucher.getString(VALUE));
						b.putString("currency", voucher.getString("currency"));
						b.putString("description", voucher.getString(DESCRIPTION));
						b.putString("requirements",voucher.getString("voucher_template_instructions"));
						b.putString("comments",voucher.getString("voucher_template_comments"));
						//TODO b.putString("holder", voucher.getString(HOLDER));
						b.putString("holder", "");
						b.putString("pin", voucher.getString(PIN));
					}
					scanActivity.putExtras(b); //Put your id to your next Intent
					//startActivity(scanActivity);
					startActivityForResult(scanActivity, 10);
					finish();
					
					
				}
				
				
				
} catch (JSONException e) {
	e.printStackTrace();
}
    }
*/


	
	private void initCamera()
	{
		try {
		Intent intent = new Intent(this, ZBarScannerActivity.class);
        intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE, Symbol.CODE128});
        startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
		} catch (Exception e) {
			
		}
	}

	
	
	
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(ScanCamera.this.CONNECTIVITY_SERVICE);
        if (cm == null)
          return false;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
          return false;
        return ni.isConnectedOrConnecting();
      } 
	
	
	private void decode()
    {
		if (isOnline()) {
			pendingValidation = true;

			ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_RING, 200);
			tg.startTone(ToneGenerator.TONE_PROP_PROMPT);

			mAsyncTaskManager = new AsyncTaskManager(this, this, getString(R.string.validating_voucher_code_));
			mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());
			mTask = new  processCode(getResources());
			mAsyncTaskManager.setupTask(mTask);

		} else {
			Toast.makeText(ScanCamera.this, R.string.no_network_connection, Toast.LENGTH_LONG).show();
		}
    }

    private class processCode extends Task {



		public processCode(Resources resources) {
			super(resources);
			// TODO Auto-generated constructor stub
		}

		private JSONObject json;
        
        
        
        @Override
        protected Boolean doInBackground(Void... arg0) {
        	json = UserFunctions.getInstance().validateQR(qrCode);
		return true;
		}
		
        public JSONObject getResult() {
            return json;
        }
		
		
		
    }


	@Override
	public void onTaskComplete(Task task) {
		if (!task.isCancelled()) {
    		JSONObject json = ((processCode) mTask).getResult();
    		processResult(json);
		}
	}
    
    
    
	private void processResult(JSONObject json)
	{
		try {
			if (json != null )
			{
				if (json.has("AuthError") == true) {
					if (json.getString("AuthError").equals("invalid_grant"))
					{	
						Toast.makeText(ScanCamera.this, R.string.incorrect_username_password, Toast.LENGTH_LONG).show(); 

						Intent login = new Intent(getApplicationContext(), Login.class);
						login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Bundle b = new Bundle();
						b.putString("GCM_regID", "");
						login.putExtras(b);
						startActivity(login);
						finish();
					} else {
						Toast.makeText(ScanCamera.this, json.getString("AuthError"), Toast.LENGTH_LONG).show();
					}
				} else {
			if (json.getString(VALIDATION_CODE) != null) {

				Intent scanActivity = new Intent(ScanCamera.this, ScanActivity.class);
				Bundle b = new Bundle();
				b.putInt("validationCode", Integer.parseInt(json.getString(VALIDATION_CODE))); 
				if (json.has("error"))
					b.putString("error", json.getString("error"));
				else
					b.putString("error", "");
				if (json.has("error_msg"))
					b.putString("error_msg", json.getString("error_msg"));
				else
					b.putString("error_msg", "");
				
				//b.putString("qrCode",qrCode);
				if (Integer.parseInt(json.getString(VALIDATION_CODE)) == 1)
				{

					JSONObject voucher = json.getJSONObject("voucher");
					String s = voucher.getString("id");
					b.putString("idVoucher", voucher.getString("id"));
					b.putString("emisor", voucher.getString(EMISOR));
					b.putString("caducity", voucher.getString(CADUCITY));
					b.putString("value", voucher.getString(VALUE));
					b.putString("currency", voucher.getString("currency"));
					b.putString("description", voucher.getString(DESCRIPTION));
					b.putString("requirements",voucher.getString("voucher_template_instructions"));
					b.putString("name",voucher.getString("voucher_template_name"));
					b.putString("comments",voucher.getString("voucher_template_comments"));
					b.putString("voucher_type",voucher.getString("voucher_template_voucher_type"));
					b.putString("cumulative_value",voucher.getString("cumulative_status"));
					b.putString("cumulative_target",voucher.getString("voucher_template_cumulative_value"));
					b.putString("nominal", voucher.getString("voucher_template_nominal_issue"));
					b.putString("beneficiary_name", voucher.getString("beneficiary_name"));
					b.putString("loyalty_points_accounting", voucher.getString("voucher_template_loyalty_points_accounting"));
					b.putString("loyalty_points_per_scan", voucher.getString("voucher_template_loyalty_points_per_scan"));
					b.putString("loyalty_money_amount", voucher.getString("voucher_template_loyalty_money_amount"));
					b.putString("loyalty_points_per_currency_unit", voucher.getString("voucher_template_loyalty_points_per_currency_unit"));
					b.putString("points", voucher.getString("points"));
					
					//TODO b.putString("holder", voucher.getString(HOLDER));
					b.putString("holder", "");
					b.putString("pin", voucher.getString(PIN));
				
				
				} 
				scanActivity.putExtras(b); //Put your id to your next Intent
				startActivityForResult(scanActivity, 10);
				pendingValidation = false;
				finish();
				
			} else {
				pendingValidation = false;
			}
			}
		}
    	} catch (JSONException e) {
    	    e.printStackTrace();
    	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    	    pendingValidation = false;
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
