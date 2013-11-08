package com.google.zxing.client.android;

import libraries.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import com.beevou.android.scanner.R;
import com.beevou.android.scanner.R.layout;
import com.beevou.android.scanner.R.menu;
import com.beevou.android.scanner.Dashboard;
import com.beevou.android.scanner.Login;
import com.beevou.android.scanner.scan.ScanActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ManualCode extends Activity {
	private EditText codeEdit;
	private static String VALIDATION_CODE = "validationCode";
    private static String EMISOR = "issuer_name";
    private static String VALUE = "value";
    private static String CURRENCY = "currency";
    private static String DESCRIPTION = "voucher_template_description";
    private static String CADUCITY = "end_date";
    private static String HOLDER = "holder";
    private static String PIN = "pin_code";
    private Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_manual_code);
		
		codeEdit = (EditText) findViewById(R.id.code);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}
	
	public void send(View v)
	{

			//Bundle b = new Bundle();
			String code = codeEdit.getText().toString();
			Log.e("scanned code", code);
			
			if (code.startsWith("0")||code.startsWith("BEEVOU"))
					{
				//checkBeevouCode(code);
				
				new Asyn_Task().execute("","");
			} else {
				Toast.makeText(this,
						 "Not a valid Beevou code.  Please try again.",
						 Toast.LENGTH_LONG).show();
			}
			
			
			
		


	}
	
	
	
	
	
    private class Asyn_Task extends AsyncTask<String, String, Void> {
        private final ProgressDialog dialog = new ProgressDialog(ManualCode.this);
        private JSONObject json;
        // can use UI thread here
        protected void onPreExecute() {
            this.dialog.setMessage("Sending...");
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
	    	//UserFunctions userFunctions = new UserFunctions();
	    	json = UserFunctions.getInstance().validateQR(codeEdit.getText().toString());
	    	    	    
		return null;
		}
		
		
		private void processResult()
		{
		try {	
			if (json.has("AuthError") == true) {
   				if (json.getString("AuthError").equals("invalid_grant"))
   				{	
   					Toast.makeText(ManualCode.this, R.string.incorrect_username_password, Toast.LENGTH_LONG).show(); 

   					Intent login = new Intent(getApplicationContext(), Login.class);
   					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
   					Bundle b = new Bundle();
   					b.putString("GCM_regID", "");
   					login.putExtras(b);
   					startActivity(login);
   					finish();
   				} else {
   					Toast.makeText(ManualCode.this, json.getString("AuthError"), Toast.LENGTH_LONG).show();
   				}
   			} else {
			if (json.getString(VALIDATION_CODE) != null) {

				Intent scanActivity = new Intent(context, ScanActivity.class);
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
				startActivity(scanActivity);
			}
   			}
    }  catch (JSONException e) {
		e.printStackTrace();
	}
		}
		
		
		
		
    }
	

}
