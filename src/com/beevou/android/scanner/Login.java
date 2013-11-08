package com.beevou.android.scanner;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import com.beevou.android.scanner.R;
import com.beevou.android.scanner.taskmanager.AsyncTaskManager;
import com.beevou.android.scanner.taskmanager.OnTaskCompleteListener;
import com.beevou.android.scanner.taskmanager.Task;
import com.google.zxing.BarcodeFormat;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import libraries.DatabaseHandler;
import libraries.UserFunctions;
 
public class Login extends Activity implements 
OnTaskCompleteListener {
    Button btnLogin;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;
 
    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "firstname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created";
    private static String USER_NAME = "username";
    //private UserFunctions userFunction = new UserFunctions();
    private String GCM_regID;
    private AsyncTaskManager mAsyncTaskManager;
    private Task mTask;
    private int oldOrientation;
    
    
    //http://damianflannery.wordpress.com/2011/06/13/integrate-zxing-barcode-scanner-into-your-android-app-natively-using-eclipse/
    static BarcodeFormat BF = BarcodeFormat.CODE_128;
    

 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
          
        
        
        Bundle b = getIntent().getExtras();
        if (b != null)
        	GCM_regID = b.getString("GCM_regID");
        
 
        // Importing all assets like buttons, text fields
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

       // DatabaseHandler db = new DatabaseHandler(getApplicationContext());
       // HashMap<String, String> userValues = db.getUserDetails();
        //userValues.get("email");   
        inputEmail.setText(Beevou_Scanner.getInstance().getUserName());

        
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        
        inputPassword.setImeActionLabel("Login", KeyEvent.KEYCODE_ENTER);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        

  
    }
    

    
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Login.this.CONNECTIVITY_SERVICE);
        if (cm == null)
          return false;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
          return false;
        return ni.isConnectedOrConnecting();
      } 
    
    public void goLogin(View v)
    {
    	if (isOnline()) {      

    		

    		


    		mAsyncTaskManager = new AsyncTaskManager(this, this, getString(R.string.logging_into_beevou));
    		mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());
    		mTask = new  loginTask(getResources());
    		mAsyncTaskManager.setupTask(mTask);

    	} else {
    		Toast.makeText(Login.this, R.string.no_network_connection, Toast.LENGTH_LONG).show();
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
    
    
    private void goin(Integer result,String keyName,String keyEmail,String keyUID, String userName )
    {
    	if(result == 1){
            // user successfully logged in
             // Store user details in SQLite Database
            // DatabaseHandler db = new DatabaseHandler(getApplicationContext());
             //JSONObject json_user = json.getJSONObject("user");

             // Clear all previous data in database
             //userFunction.logoutUser(getApplicationContext());
    		
    		UserFunctions.getInstance().logoutUser(getApplicationContext());
    		
    		//SharedPreferences prefs	= this.getSharedPreferences("BVOU1", 0);

    		String user = inputEmail.getText().toString();
    		String password = inputPassword.getText().toString();
    		
    		/*
    		 * prefs.edit().putString("001","BEEVOU ANDROID APPLICATION").commit();
    		prefs.edit().putString("002",email).commit();
    		prefs.edit().putString("003",password).commit();
    		*/
    		
    		Beevou_Scanner.getInstance().setUserName(user);
        	Beevou_Scanner.getInstance().setUserPassword(password);
        	
            // db.addUser(keyName, keyEmail, keyUID, "2012-01-01",userName); 
             
             

             // Launch Dashboard Screen
             Intent dashboard = new Intent(getApplicationContext(), Dashboard.class);
             Bundle b = new Bundle();
				b.putBoolean("logged", true);
				

             // Close all views before launching Dashboard
             dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             dashboard.putExtras(b);
               
             
             startActivity(dashboard);

             // Close Login Screen
             finish();
         }else{
             // Error in login
        	 final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        	 alertDialog.setMessage(R.string.incorrect_username_password);
        	 alertDialog.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
        	 //alertDialog.setIcon(R.drawable.icon);
        	 alertDialog.show();
        	 
             loginErrorMsg.setText(R.string.incorrect_username_password);
         }
    }
    
    
    private class loginTask extends Task {
        public loginTask(Resources resources) {
			super(resources);
			// TODO Auto-generated constructor stub
		}

        private JSONObject json;


      

        @Override
        protected Boolean doInBackground(Void... arg0) {
        	try {	
        		String email = inputEmail.getText().toString();
        		String password = inputPassword.getText().toString();
        		json = UserFunctions.getInstance().loginUser(email, password,GCM_regID);
        	} catch (Exception e) {
        		//Log.e("background Error loginTask",e.getMessage());
        		Log.e("background Error loginTask","");
        	}
	        
	       
		return true;   
		}
        
        public JSONObject getResult() {
            return json;
        }
        
        
    }
    
    public void forgotPass(View v)
    {
    	String url = "https://beevou.net/m/users/remember_password";
    	Intent i = new Intent(Intent.ACTION_VIEW); // Create a new intent - stating you want to 'view something'
        i.setData(Uri.parse(url));  // Add the url data (allowing android to realise you want to open the browser)
        startActivity(i); // Go go go! 
    }
    
    public void joinBeevou(View v)
    {
    	String url = "https://beevou.net/m/users/signup";
    	Intent i = new Intent(Intent.ACTION_VIEW); // Create a new intent - stating you want to 'view something'
        i.setData(Uri.parse(url));  // Add the url data (allowing android to realise you want to open the browser)
        startActivity(i); // Go go go! 
    }


    @Override
    public void onTaskComplete(Task task) {


    	if (!task.isCancelled()) {
    		JSONObject json = ((loginTask) mTask).getResult();
   		try {
    	
   			if (json.has("AuthError") == true) {
   				if (json.getString("AuthError").equals("invalid_grant"))
   				{	
   					Toast.makeText(Login.this, R.string.incorrect_username_password, Toast.LENGTH_LONG).show(); 

   					Intent login = new Intent(getApplicationContext(), Login.class);
   					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
   					Bundle b = new Bundle();
   					b.putString("GCM_regID", GCM_regID);
   					login.putExtras(b);
   					startActivity(login);
   					finish();
   				} else {
   					Toast.makeText(Login.this, json.getString("AuthError"), Toast.LENGTH_LONG).show();
   				}
   			} else {
   				if (json.getString(KEY_SUCCESS) != null) {
   					Integer res = Integer.parseInt(json.getString(KEY_SUCCESS));
   					if (res == 1)
   					{
   						JSONObject json_user = json.getJSONObject("user"); 
   						goin(res,json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(USER_NAME));                        
   					} else {
   						goin(res,"", "", "","");	
   					}

   				}
   			}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
   			
    	}
    }
    
}
