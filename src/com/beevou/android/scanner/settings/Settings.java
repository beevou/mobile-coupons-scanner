package com.beevou.android.scanner.settings;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.beevou.android.scanner.R;
import com.beevou.android.scanner.R.id;
import com.beevou.android.scanner.R.layout;
import com.beevou.android.scanner.Beevou_Scanner;
import com.beevou.android.scanner.Login;
import com.beevou.android.scanner.taskmanager.AsyncTaskManager;
import com.beevou.android.scanner.taskmanager.OnTaskCompleteListener;
import com.beevou.android.scanner.taskmanager.Task;

import libraries.BeevouFunctions;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity implements 
OnTaskCompleteListener {
    
    private AsyncTaskManager mAsyncTaskManager;
    private Task mTask;
    private CheckBox automaticLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settings);

		TextView userName = (TextView) findViewById(R.id.userName);
		automaticLogin = (CheckBox) findViewById(R.id.automaticLogin);
        
        userName.setText(Beevou_Scanner.getInstance().getUserName());
        String autoLogin = Beevou_Scanner.getInstance().getLoginMode();
        
        	if (autoLogin.equals("AUTO"))
        	{ 
        		automaticLogin.setChecked(true);
        	} else{
        		automaticLogin.setChecked(false);
        	}
        goGetCredits();
		
	}

	
	public void automaticLoginClick(View v)
	{
		if (automaticLogin.isChecked() == true)
		{
			Beevou_Scanner.getInstance().setLoginMode("AUTO");
		} else {
			Beevou_Scanner.getInstance().setLoginMode("MANUAL");	
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		
		return true;
	}
	
    public void back(View v)
    {
    	finish();
    }
	
	
	public void myAccountClick(View v)
	{
		
	}
	
	public void exitClick(View v)
	{

    	BeevouFunctions.getInstance().logoutUser(getApplicationContext());
        Intent login = new Intent(getApplicationContext(), Login.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        // Closing dashboard screen
        finish();
	}
	
	public void unlinkClick(View v)
	{
		
	}
	
	
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Settings.this.CONNECTIVITY_SERVICE);
        if (cm == null)
          return false;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
          return false;
        return ni.isConnectedOrConnecting();
      } 
	
    public void goGetCredits()
    {
    	if (isOnline()) {      

    		mAsyncTaskManager = new AsyncTaskManager(this, this, getString(R.string.getting_credits));
    		mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());
    		mTask = new  getCredits(getResources());
    		mAsyncTaskManager.setupTask(mTask);

    	} else {
    		Toast.makeText(Settings.this, R.string.no_network_connection, Toast.LENGTH_LONG).show();
    	}
    }
	
	
    private class getCredits extends Task {
        public getCredits(Resources resources) {
			super(resources);
			
		}

        private JSONObject json;

        @Override
        protected Boolean doInBackground(Void... arg0) {
        	try {	
        		json = BeevouFunctions.getInstance().getUserCredits();
        	} catch (Exception e) {
        		Log.e("background Error getting credits","");
        	}
	        
	       
		return true;   
		}
        
        public JSONObject getResult() {
            return json;
        }
        
        
    }

	@Override
	public void onTaskComplete(Task task) {
		if (!task.isCancelled()) {
    		JSONObject json = ((getCredits) mTask).getResult();
    		
			try {
				if (json.has("AuthError") == true) {
					if (json.getString("AuthError").equals("invalid_grant"))
					{	
						Toast.makeText(Settings.this, R.string.incorrect_username_password, Toast.LENGTH_LONG).show(); 

						Intent login = new Intent(getApplicationContext(), Login.class);
						login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Bundle b = new Bundle();
						b.putString("GCM_regID", "");
						login.putExtras(b);
						startActivity(login);
						finish();
					} else {
						Toast.makeText(Settings.this, json.getString("AuthError"), Toast.LENGTH_LONG).show();
					}
				} else {
				TextView credits = (TextView) findViewById(R.id.credits);
				credits.setText(String.format("%s "+getString(R.string.of)+" %s", json.getString("user_credits"),json.getString("total_credits")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
   			
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
