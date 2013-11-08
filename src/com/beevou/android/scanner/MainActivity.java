package com.beevou.android.scanner;

import java.io.IOException;
import java.util.Timer;

import org.json.JSONException;
import org.json.JSONObject;

import libraries.UserFunctions;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.beevou.android.authentication.AuthenticatorActivity;
import com.beevou.android.scanner.scan.Discount;
import com.beevou.android.scanner.scan.ScanActivity;
import com.beevou.android.scanner.R;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.google.android.gcm.GCMRegistrar;
import android.provider.Settings.Secure;
import static com.beevou.android.GCMPushNotifications.CommonUtilities.EXTRA_MESSAGE;


import net.sourceforge.zbar.Symbol;

public class MainActivity extends Activity {

    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;
    private static String VALIDATION_CODE = "validationCode";
    private static String EMISOR = "emisor";
    private static String VALUE = "value";
    private static String DESCRIPTION = "description";
    private static String CADUCITY = "caducity";
    private static String HOLDER = "holder";
    private static String PIN = "pin";  
    ConnectionDetector cd;
    AlertDialogManager alert = new AlertDialogManager();
    private String GCM_regID;
    
    String SENDER_ID ="1022691900347";
    
    
    ////UserFunctions userFunctions;
    Button btnLogout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        
        switch (getResources().getDisplayMetrics().densityDpi) {
        case DisplayMetrics.DENSITY_LOW: //36x36 imagenes icono
        	Log.v("density", "DENSITY_LOW"); 
            break;
        case DisplayMetrics.DENSITY_MEDIUM: //48x48 imagenes icono
        	Log.v("density", "DENSITY_MEDIUM"); 
            break;
        case DisplayMetrics.DENSITY_HIGH:    //72x72 imagenes icono
        	Log.v("density", "DENSITY_HIGH"); 
            break;
        case DisplayMetrics.DENSITY_XHIGH:  //96x96 imagenes icono
        	Log.v("density", "DENSITY_XHIGH"); 
            break;
        }
        

         
        Handler handler = new Handler();
        handler.postDelayed(getRunnableStartApp(), 2000);
        
        
        
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
          GCMRegistrar.register(this, SENDER_ID);
          GCM_regID = GCMRegistrar.getRegistrationId(this);
        } else {
          Log.v("GCM Registration", "Already registered");
          Log.v("regID",regId);
          GCM_regID = regId;
        }
    }
    

    private Runnable getRunnableStartApp(){
    	Runnable runnable = new Runnable(){
    	public void run(){
            String android_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
            Log.e("android_id", android_id);
            ////userFunctions = new UserFunctions();
            if (!Beevou_Scanner.getInstance().getUserName().equals("") && Beevou_Scanner.getInstance().getLoginMode().equals("AUTO"))
            {
            	Intent dashboard = new Intent(getApplicationContext(), Dashboard.class);
                Bundle b = new Bundle();
   				b.putBoolean("logged", true);
                dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                dashboard.putExtras(b);
                startActivity(dashboard);
            } else {
            	Intent login = new Intent(getApplicationContext(), Login.class);
            	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	Bundle b = new Bundle();
            	b.putString("GCM_regID", GCM_regID);
            	login.putExtras(b);
            	startActivity(login);
            }
    	}
    	};
    	return runnable;
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
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
   }
   
   private Activity getContext()
   {
	   return this;
   }
   
   
   public String getUserNick()
   {
	   SharedPreferences prefs	= this.getSharedPreferences("BVOU1", 0);
	   return prefs.getString("002",null);
   }
   
   public String getUserPass()
   {
	   SharedPreferences prefs	= this.getSharedPreferences("BVOU1", 0);
	
	   return prefs.getString("003",null);
   }
   
   /**
    * Receiving push messages
    * */
   private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
       @Override
       public void onReceive(Context context, Intent intent) {
           String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
           // Waking up mobile if it is sleeping
           WakeLocker.acquire(getApplicationContext());

           /**
            * Take appropriate action on this message
            * depending upon your app requirement
            * For now i am just displaying it on the screen
            * */

           // Showing received message
           //lblMessage.append(newMessage + "\n");
           Toast.makeText(getApplicationContext(), R.string.new_message_ +" "+ newMessage, Toast.LENGTH_LONG).show();

           // Releasing wake lock
           WakeLocker.release();
       }
   };
   
}
