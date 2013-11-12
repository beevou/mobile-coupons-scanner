package libraries;

import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.beevou.android.scanner.Beevou_Scanner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
 
public class UserFunctions {
 
	private static UserFunctions mInstance= null;
    private JSONParser jsonParser;  
    
    
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String validateQR_tag = "validateQR";
    private String userNick;
    private String userPass;
    private String regID;
    private Timestamp lastInteraction;
    private String accessToken = null;
    private String refreshToken = null;
    private Calendar tokenExpiresIn = Calendar.getInstance();
    private java.util.Date lastOperationOn;
	private int expirationSecs = 600;
	//private static String beevouURL = "https://beevou.net/api";
	private static String beevouURL = "http://sandbox.beevou.net/api";
	//private static String baseURL = "https://beevou.net";
	private static String baseURL = "http://sandbox.beevou.net";
	static String clientID = "NTE2M2ViMTM1MDVmYTZl";
	static String clientSecret = "cff1423e6c443bdcc1a38b11554ecb21b781ed9d";
	
	/*
	private static String beevouURL = "http://sandbox.beevou.net/api";
	static String clientID = "NTFiYjAxZjAzMGU2NWYx";
	static String clientSecret = "58d44d3c6b9938a4d952b3b440a7c3da3ac4b620";
	*/
    
    // constructor
    protected UserFunctions(){
        jsonParser = new JSONParser();
       // new Timer().scheduleAtFixedRate(new refreshTokenTask(), 300000, 300000);
    }
    
    

    public static synchronized UserFunctions getInstance(){
    	if(null == mInstance){
    		mInstance = new UserFunctions();
    	}
    	return mInstance;
    }
 
    private JSONObject authorizationError()
    {
	JSONObject json = null;
	
	 	try {
	 		json = new JSONObject("{'AuthError':'invalid_grant'}");	
	 	} catch (JSONException e) {
	 		Log.e("JSON Parser", "Error parsing data " + e.toString());
	 	}
		
	 return json;	
    }
    
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String userName, String password,String GCM_regID){
    	userNick = userName;
        userPass = password;
        
        Log.i("loginUser","entering login"); 
        Log.i("userNick",userNick);
        
        
      	String storedUserNick = Beevou_Scanner.getInstance().getUserName();
      	String storedUserPass = Beevou_Scanner.getInstance().getUserPassword();
      	
      	Log.i("storedUserNick",storedUserNick);
      	
      	if (userNick.equals("") && userPass.equals(""))
      	{ //Recover user and pass from preferences
      		if (!storedUserNick.equals(""))
      			userNick = storedUserNick;
      		if (!storedUserPass.equals(""))
      			userPass = storedUserPass;
      	}

        if (!storedUserNick.equals(userNick) &&! storedUserPass.equals(userPass))
        {  //Has changed
        	Beevou_Scanner.getInstance().setUserName(userNick);
        	Beevou_Scanner.getInstance().setUserPassword(userPass);
        	Log.i("saved login","saved login");
        }
        
        
        
        
        regID = GCM_regID;
    	
        
        JSONObject json = null;
        
        JSONObject token = loginUserToken(userName,password);
        
        if (token != null)
        {
        	try {
        		accessToken = token.getString("access_token");
        		//beevouApp.setAccessToken(accessToken);
        		refreshToken = token.getString("refresh_token");
        		//beevouApp.setRefreshToken(refreshToken);
        		tokenExpiresIn.add(Calendar.SECOND, expirationSecs);
        	} catch (JSONException e) {
        		e.printStackTrace();
        		//There is an error or an invalid grant
        		//{"error":"invalid_grant"}n
        		//Lets Go to the Login Page
        	}
        }
  
        

        
       if (accessToken != null)
       {
    	
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        
        
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("username", userName));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("device", "ANDROID"));
        //for this below the minimun is ver 9 now we are in 8
        //params.add(new BasicNameValuePair("device_serial", Build.SERIAL));
        //Log.i("TAG","android.os.Build.SERIAL: " + Build.SERIAL);
        params.add(new BasicNameValuePair("device_serial", ""));
        params.add(new BasicNameValuePair("device_os_version", Build.VERSION.RELEASE));
        params.add(new BasicNameValuePair("device_push_id", GCM_regID));
        params.add(new BasicNameValuePair("device_brand", Build.BRAND));
        params.add(new BasicNameValuePair("device_model", Build.MODEL));
        /*
        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
        String number = tm.getLine1Number();
        */
        
        
        Log.i("email", userName);
        Log.i("device", "ANDROID");
        Log.i("device_serial", "");
        Log.i("device_os_version", Build.VERSION.RELEASE);
        if (GCM_regID != null)
        	Log.i("device_push_id", GCM_regID);
        Log.i("device_brand", Build.BRAND);
        Log.i("device_model", Build.MODEL);
        json = jsonParser.getJSONFromUrl(beevouURL+"/users/login/?access_token="+accessToken , params);
       }
        return json;
    }
    
    
    
    
    
    public JSONObject loginUserToken(String userName, String password)
    {
    	
    	
    	JSONObject json = jsonParser.getJSONFromUrl(baseURL+"/oauth/token?grant_type=password&username="+userName+"&password="+password+"&client_id="+clientID+"&client_secret="+clientSecret);
		return json;
    }
    
    public JSONObject refreshToken()
    {
    	
    	JSONObject json = null;
    	
    	try
    	{
    	
    	json = jsonParser.getJSONFromUrl(baseURL+"/oauth/token?grant_type=refresh_token&refresh_token="+refreshToken+"&client_id="+clientID+"&client_secret="+clientSecret);
    	} catch (Exception e) {
    		e.printStackTrace();
    		
    	}
    	
    	return json;
    }
        
    
    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(beevouURL+"", params);
        // return json
        return json;
    }
 
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }
 
    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
    	Beevou_Scanner.getInstance().setUserName("");
    	Beevou_Scanner.getInstance().setUserPassword("");
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
    
    public JSONObject validateQR(String qrCode){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", validateQR_tag));
        params.add(new BasicNameValuePair("qrCode", qrCode));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL +"/vouchers/validate_voucher?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }
        return json;
    }
    
    public JSONObject discountQR(String voucherID, String pincode){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "discountQR"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        params.add(new BasicNameValuePair("pinCode",pincode));
        Log.i("voucherID", voucherID);
        Log.i("pin code for discount", pincode);
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/vouchers/discount_voucher?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }
        return json;
    }
    
    public JSONObject getMyVouchers(int orderBy, int searchType,int page){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "getMyVouchers"));
        params.add(new BasicNameValuePair("orderBy", String.valueOf(orderBy)));
        params.add(new BasicNameValuePair("searchPage", String.valueOf(page)));
        Log.i("ORDERBY", String.valueOf(orderBy));
        Log.i("SEARCHTYPE", String.valueOf(searchType));
        params.add(new BasicNameValuePair("searchType",  String.valueOf(searchType)));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/vouchers/get_user_vouchers?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }
        return json;
    }
    
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    
    public JSONObject transferVoucher(String voucherID, String transferEmail){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "transferVoucher"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        params.add(new BasicNameValuePair("transferEmail",  transferEmail));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/vouchers/transfer_voucher?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    public JSONObject deleteVoucher(String voucherID){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "deleteVoucher"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/vouchers/delete_voucher?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    public JSONObject savePin(String voucherID, String pin){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "savePin"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        params.add(new BasicNameValuePair("pin",  pin));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/vouchers/save_voucher_pin?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    public JSONObject removePin(String voucherID){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "removePin"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/vouchers/remove_voucher_pin?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    public JSONObject checkPin(String voucherID, String pin){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "checkPin"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        params.add(new BasicNameValuePair("pinCode", pin));
        Log.i("voucherID", voucherID);
        Log.i("pinCode", pin);
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/vouchers/check_pin_code?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    
    public JSONObject getTemplates(int page){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "get_user_templates"));
        params.add(new BasicNameValuePair("searchType", "1"));
        params.add(new BasicNameValuePair("searchPage", String.valueOf(page)));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/voucherstemplates/get_user_templates?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    public JSONObject simpleLaunch(String idtemplate, String email, String firstName, String lastName,String lists){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "simple_Launch"));
        params.add(new BasicNameValuePair("vouchers_template_id", String.valueOf(idtemplate)));
        params.add(new BasicNameValuePair("email_to",  String.valueOf(email)));
        params.add(new BasicNameValuePair("firstname",  String.valueOf(firstName)));
        params.add(new BasicNameValuePair("lastname",  String.valueOf(lastName)));
        params.add(new BasicNameValuePair("lists",  String.valueOf(lists)));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/voucherslaunches/quick_launch?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    public JSONObject checkBeneficiary(String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "check_beneficiary"));
        params.add(new BasicNameValuePair("email",  String.valueOf(email)));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/users/check_if_exists?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    

    public JSONObject getLaunchedVouchers(int orderBy, int searchType, int page){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "launched_vouchers"));
        params.add(new BasicNameValuePair("orderBy", String.valueOf(orderBy)));
        params.add(new BasicNameValuePair("searchPage", String.valueOf(page)));
        Log.i("ORDERBY", String.valueOf(orderBy));
        Log.i("SEARCHTYPE", String.valueOf(searchType));
        params.add(new BasicNameValuePair("searchType",  String.valueOf(searchType)));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/voucherslaunches/launched_vouchers?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    
    public JSONObject getScanedVouchers(int orderBy, int searchType, int page){
    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "launched_vouchers"));
        params.add(new BasicNameValuePair("orderBy", String.valueOf(orderBy)));
        params.add(new BasicNameValuePair("searchPage", String.valueOf(page)));
        Log.i("ORDERBY", String.valueOf(orderBy));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/reader/get_readed_vouchers?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    public JSONObject backToHolder(String voucherID){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "discountQR"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        Log.i("voucherID", voucherID);
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/reader/return_readed_voucher?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    public JSONObject deleteLaunched(String voucherID){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "discountQR"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        Log.i("voucherID", voucherID);
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/voucherslaunches/delete_launched_voucher?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    public JSONObject lockLaunched(String voucherID){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "discountQR"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        Log.i("voucherID", voucherID);
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/voucherslaunches/lock_launched_voucher?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    public JSONObject unLockLaunched(String voucherID){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "discountQR"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        Log.i("voucherID", voucherID);
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/voucherslaunches/unlock_launched_voucher?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    public JSONObject resendVoucher(String voucherID){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "discountQR"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        Log.i("voucherID", voucherID);
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/vouchers/resend_voucher?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }
    
    public JSONObject getNumberNotifications(){

    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/notifications/get_pending_notifications?access_token="+getToken(), params);
        } else {
        	json = authorizationError();	
        }
        return json;
    }
    

    
public JSONObject getUserNotifications(int orderBy, int searchType, int page){
    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "launched_vouchers"));
        params.add(new BasicNameValuePair("orderBy", String.valueOf(orderBy)));
        params.add(new BasicNameValuePair("searchPage", String.valueOf(page)));
        params.add(new BasicNameValuePair("searchType", String.valueOf(searchType)));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/notifications/get_user_notifications?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
    }

public JSONObject setReadedNotification(String notificationID){
	
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("tag", "Readed Notification"));
    params.add(new BasicNameValuePair("notificationID", notificationID));
    String theToken = getToken();
    JSONObject json;
    if (theToken != null)
    {
    	json = jsonParser.getJSONFromUrl(beevouURL+"/notifications/set_readed?access_token="+theToken, params);
    } else {
    	json = authorizationError();	
    }

    return json;
}

public JSONObject acceptNotification(String notificationID){
	
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("tag", "Readed Notification"));
    params.add(new BasicNameValuePair("notificationID", notificationID));
    Log.i("notificationID", notificationID);
    String theToken = getToken();
    JSONObject json;
    if (theToken != null)
    {
    	json = jsonParser.getJSONFromUrl(beevouURL+"/notifications/accept?access_token="+theToken, params);
    } else {
    	json = authorizationError();	
    }

    return json;
}

public JSONObject rejectNotification(String notificationID){
	
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("tag", "Readed Notification"));
    params.add(new BasicNameValuePair("notificationID", notificationID));
    String theToken = getToken();
    JSONObject json;
    if (theToken != null)
    {
    	json = jsonParser.getJSONFromUrl(beevouURL+"/notifications/reject?access_token="+theToken, params);
    } else {
    	json = authorizationError();	
    }

    return json;
}

    
public String viewVoucher(String voucherID){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "discountQR"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        Log.i("voucherID", voucherID);
        //TODO:Token error
        return jsonParser.getStringFromUrl(beevouURL+"/vouchers/view_voucher?access_token="+getToken(), params);
}
    
public JSONObject getUserData(){
    	
    String theToken = getToken();
    JSONObject json;
    if (theToken != null)
    {
    	json = jsonParser.getJSONFromUrl(beevouURL+"/users/get_user_data?access_token="+theToken);
    } else {
    	json = authorizationError();	
    }

    return json;
}
    
public JSONObject getUserProfile(){
    	
	
    String theToken = getToken();
    JSONObject json;
    if (theToken != null)
    {
    	json = jsonParser.getJSONFromUrl(beevouURL+"/users/get_profile?access_token="+theToken);
    } else {
    	json = authorizationError();	
    }
	

   return json;
}
    
public JSONObject getUserPublicData(){
    String theToken = getToken();
    JSONObject json;
    if (theToken != null)
    {
    	json = jsonParser.getJSONFromUrl(beevouURL+"/affiliates/get_public_data?access_token="+theToken);
    } else {
    	json = authorizationError();	
    }
    

        return json;
}
    
public JSONObject getUserCredits(){
    String theToken = getToken();
    JSONObject json;
    if (theToken != null)
    {
    	json = jsonParser.getJSONFromUrl(beevouURL+"/userscredits/get_user_credits?access_token="+theToken);
    } else {
    	json = authorizationError();	
    }

    return json;
}
    
public JSONObject restoreVoucherTransfer(String voucherID){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "restoreVoucherTransfer"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/vouchers/restore_voucher?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
}
    
public JSONObject acceptVoucherTransfer(String voucherID){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "restoreVoucherTransfer"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/vouchers/accept_transfer?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
}
    
public JSONObject rejectVoucherTransfer(String voucherID){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "restoreVoucherTransfer"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/vouchers/reject_transfer?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }

        return json;
}
    
    
private String getToken()
    {
    	java.util.Date currentDate = new Date();
    	
    	Calendar now = GregorianCalendar.getInstance();
    	
    	
    	if (now.compareTo(tokenExpiresIn) >0 )

    	{
    		
    		this.loginUser(Beevou_Scanner.getInstance().getUserName(), Beevou_Scanner.getInstance().getUserPassword(), this.regID);
    	} else {

    		if (lastOperationOn != null)
    		{

    			if ((currentDate.getTime() - lastOperationOn.getTime()) >300000)
    			{
    				userToken();
    			}
    			
    		} else {
    			userToken();
    		}
    	}
		
		lastOperationOn = new Date();

		tokenExpiresIn.add(Calendar.SECOND, expirationSecs);
		
		return accessToken;
}
    
    
    
    private void userToken()
    {
          JSONObject token = loginUserToken(this.userNick,this.userPass);
        
        /*
         * 
         * {"access_token":"1a0842d56aa9395651c9c8a0b8a9a38636d4dd3d","expires_in":3600,"token_type":"bearer","scope":null,"refresh_token":"0c1bdc19111bf4184a573a4b87660378ab6fda5d"}
         */
        
         
        
        if (token != null)
        {
        	try {
        		accessToken = token.getString("access_token");
        		refreshToken = token.getString("refresh_token");
        		//int expirationSecs = token.getInt("expires_in");
        		tokenExpiresIn.add(Calendar.SECOND, expirationSecs);		
        				
        	} catch (JSONException e) {
        		e.printStackTrace();
        	}
        }
    }
    
    
    
    class refreshTokenTask extends TimerTask {
    	   public void run() {
    		   JSONObject token = refreshToken(); 
    	        if (token != null)
    	        {
    	        	try {
    	        		accessToken = token.getString("access_token");
    	        		refreshToken = token.getString("refresh_token");
    	        	} catch (JSONException e) {
    	        		e.printStackTrace();
    	        	}
    	        }
    		   
    		   
    	   }
    	}
    
    public JSONObject addRewards(String voucherID, String value){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "addReward"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        params.add(new BasicNameValuePair("value",value));
        Log.i("voucherID", voucherID);
        Log.i("value for the points", value);
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/vouchers/add_rewards?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }
        return json;
    }
    
    
    public JSONObject removeRewards(String voucherID, String value){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "removeReward"));
        params.add(new BasicNameValuePair("voucherID", voucherID));
        params.add(new BasicNameValuePair("rewardPoints",value));
        Log.i("voucherID", voucherID);
        Log.i("value for the points", value);
        String theToken = getToken();
        JSONObject json;
        if (theToken != null)
        {
        	json = jsonParser.getJSONFromUrl(beevouURL+"/vouchers/remove_rewards?access_token="+theToken, params);
        } else {
        	json = authorizationError();	
        }
        return json;
    }
 
 
}
