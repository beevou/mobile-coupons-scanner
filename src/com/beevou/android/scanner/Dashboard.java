package com.beevou.android.scanner;

import org.json.JSONException;
import org.json.JSONObject;

import libraries.UserFunctions;

import com.beevou.android.scanner.R;
import com.beevou.android.scanner.notifications.BeevouNotifications;
import com.beevou.android.scanner.scan.ScanCamera;
import com.beevou.android.scanner.settings.Settings;
import com.beevou.android.scanner.taskmanager.AsyncTaskManager;
import com.beevou.android.scanner.taskmanager.OnTaskCompleteListener;
import com.beevou.android.scanner.taskmanager.Task;



import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Dashboard extends Activity implements 
OnTaskCompleteListener {
	    
    private static Boolean logged = false;
    private LinearLayout mainLayout;
    
    ///////
    ConnectionDetector cd;
    AlertDialogManager alert = new AlertDialogManager();
    private String GCM_regID;
    private AsyncTaskManager mAsyncTaskManager;
    private Task mTask;
    
    String SENDER_ID ="1022691900347";
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
         

        setContentView(R.layout.activity_dashboard);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        
        
        if (isOnline()) {
        mAsyncTaskManager = new AsyncTaskManager(this, this, getString(R.string.getting_unreaded_notifications));
        mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());
        mTask = new  getNumberNotifications(getResources());
        mAsyncTaskManager.setupTask(mTask);
    } else {
        Toast.makeText(Dashboard.this, R.string.no_network_connection, Toast.LENGTH_LONG).show();
      }
     
        
        //Se supone que con esto se quita la barra de menu de mi HTC pero habria que implementarlo con reflection 
        /*
        final View mainView = findViewById(R.id.scroll);
        mainView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

        //Register a listener for when the status bar is shown/hidden:
        final Context context = getApplicationContext();
        mainView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener () {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility == View.SYSTEM_UI_FLAG_VISIBLE)) {
                    //Do stuff here...pause the video/game?
                } else {
                    //Do other stuff here..resume the video/game?
                }
            }
        });
        */
       // setContentView(R.layout.activity_dashboard);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
       // ((Beevou) this.getApplication()).setAccessToken(value); 
        
    }
    
    
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Dashboard.this.CONNECTIVITY_SERVICE);
        if (cm == null)
          return false;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
          return false;
        return ni.isConnectedOrConnecting();
      } 
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
             
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    
    
    private Runnable getRunnableStartApp(){
    	Runnable runnable = new Runnable(){
    	public void run(){
            String android_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
            Log.e("android_id", android_id);
            ////userFunctions = new UserFunctions();
            Intent login = new Intent(getApplicationContext(), Login.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle b = new Bundle();
			b.putString("GCM_regID", GCM_regID);
			login.putExtras(b);
            startActivity(login);
    	}
    	};
    	return runnable;
    	} 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        return true;
    }

    
    public void scanedVouchersList(View v) {
        Intent intent = new Intent(this, com.beevou.android.scanner.scan.scanedVouchersList.class);
    	startActivity(intent);
    	
    }
    
    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
    
    public void launchQRScanner(View v) {
    	
    	
        if (isCameraAvailable()) {
            /*Intent intent = new Intent(this, ZBarScannerActivity.class);
            intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE, Symbol.CODE128});
            startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
        	*/
        	//startActivity(new Intent(Dashboard.this, CaptureActivity.class));
        	startActivity(new Intent(Dashboard.this, ScanCamera.class));
        	 
        } else {
            Toast.makeText(this, R.string.rear_facing_camera_unavailable, Toast.LENGTH_SHORT).show();
        }
     
        
    }
    
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    		case ZBAR_SCANNER_REQUEST:
    			//case ZBAR_QR_SCANNER_REQUEST:
    			if (resultCode == RESULT_OK) {
    				//toneMessage();
    				//this.setRequestedOrientation(defaultOrientation);
    				qrCode = data.getStringExtra(ZBarConstants.SCAN_RESULT);
    					new DiscountAsyn_Task().execute("","");
    				break;
    			}
    	}
    	
		
    }
*/
    
    
    
    
    
    
    private class getNumberNotifications extends Task {
    	
        public getNumberNotifications(Resources resources) {
			super(resources);
			// TODO Auto-generated constructor stub
		}


		private JSONObject json;

        
        
		@Override
		protected Boolean doInBackground(Void... arg0) {
	    	////UserFunctions userFunctions = new UserFunctions();
	    	//json = userFunctions.getNumberNotifications();
	    	json = UserFunctions.getInstance().getNumberNotifications();    	    
		return null;
		}
		
		
		public JSONObject getResult() {
            return json;
        }
    }
    
    
    public void myAccountClick(View v)
    {
    	Intent myaccount = new Intent(this, Settings.class);
    	startActivity(myaccount);
    	
    }
    
    public void notificationsClick(View v)
    {
    	Intent notifications = new Intent(this, BeevouNotifications.class);
    	startActivity(notifications);
    	
    }


	@Override
	public void onTaskComplete(Task task) {
		
		if (!task.isCancelled()) {
			JSONObject json = ((getNumberNotifications) mTask).getResult();
			
			try {
				
				if (json.has("AuthError") == true) {
					if (json.getString("AuthError").equals("invalid_grant"))
					{	
						 Toast.makeText(Dashboard.this, R.string.incorrect_username_password, Toast.LENGTH_LONG).show(); 
						 
						 Intent login = new Intent(getApplicationContext(), Login.class);
			            	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			            	Bundle b = new Bundle();
			            	b.putString("GCM_regID", GCM_regID);
			            	login.putExtras(b);
			            	startActivity(login);
			            	finish();
					} else {
						 Toast.makeText(Dashboard.this, json.getString("AuthError"), Toast.LENGTH_LONG).show();
					}
				} else {
					if (json.getString("pending_notifications") != null) {
						int pendingNotifications = Integer.parseInt(json.getString("pending_notifications"));
						Log.e("pending notifications ",json.getString("pending_notifications"));
						if (pendingNotifications > 0)
						{		
							ImageButton notificationsButton = (ImageButton) findViewById(R.id.notificationsButton);
							notificationsButton.setBackgroundResource(R.drawable.corporate_notifications_red_button);
						}
					} 

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
