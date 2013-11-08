package com.beevou.android.scanner.notifications;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import libraries.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beevou.android.scanner.R;
import com.beevou.android.scanner.R.drawable;
import com.beevou.android.scanner.R.id;
import com.beevou.android.scanner.R.layout;
import com.beevou.android.scanner.R.menu;
import com.beevou.android.scanner.Login;
import com.beevou.android.scanner.taskmanager.AsyncTaskManager;
import com.beevou.android.scanner.taskmanager.OnTaskCompleteListener;
import com.beevou.android.scanner.taskmanager.Task;

//import com.beevou.android.asynctaskmanager.AsyncTaskManager;
//import com.beevou.android.asynctaskmanager.OnTaskCompleteListener;
//import com.beevou.android.asynctaskmanager.TrackableTask;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class BeevouNotifications extends Activity implements 
OnTaskCompleteListener {

	private ArrayList<HashMap<String, String>> notificationslist = new ArrayList<HashMap<String, String>>();
	private Context mContext;
	private int orderBy = 0;
	private int searchType = 0;
	private int totalPages = 0;
	private int totalItems = 0;
	private int lastPage = 0;
	//private Button btnLoadMore;
    private LinearLayout loadMoreLayout;
    private LinearLayout mainLayout;
    private TextView noVouchersFound;
    private ListView lv;
    private Integer lastClicked;
    private AsyncTaskManager mAsyncTaskManager;
    private Task mTask;
   // private int oldOrientation;
    
    
	public static long daysBetween(Calendar startDate, Calendar endDate) {  
  	  Calendar date = (Calendar) startDate.clone();  
  	  long daysBetween = 0;  
  	  while (date.before(endDate)) {  
  	    date.add(Calendar.DAY_OF_MONTH, 1);  
  	    daysBetween++;  
  	  }  
  	  return daysBetween;  
  	} 
	
    public void back(View v)
    {
    	finish();
    }
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
		setContentView(R.layout.activity_beevou_notifications);
		
		
		lv = (ListView) findViewById(R.id.list);
		
		notificationslist.clear();		
		noVouchersFound = new TextView(this);
        noVouchersFound.setTextAppearance(this, android.R.attr.textAppearanceLarge);
        noVouchersFound.setBackgroundColor(Color.WHITE);
        noVouchersFound.setTextColor(Color.GRAY);
        noVouchersFound.setTextSize(24);
        noVouchersFound.setText(R.string.no_notifications_found_);
        
        
        float scale = this.getResources().getDisplayMetrics().density;
        
        loadMoreLayout = new LinearLayout(this);
        loadMoreLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        loadMoreLayout.setPadding(0, 5 * Math.round(scale), 0, 5 * Math.round(scale));
        loadMoreLayout.setBackgroundResource(R.drawable.corporate_gray);
    	Button btnLoadMore = new Button(this);
        btnLoadMore.setText(R.string.load_more_);
        btnLoadMore.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        btnLoadMore.setBackgroundResource(R.drawable.corporate_color_button);
        
        
        btnLoadMore.setPadding(25 * Math.round(scale),10 * Math.round(scale),25 * Math.round(scale),10 * Math.round(scale));
        btnLoadMore.setTextAppearance(this, android.R.attr.textAppearanceLarge);
        btnLoadMore.setTextColor(Color.WHITE);
        loadMoreLayout.addView(btnLoadMore);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	getNotifications();
            }
        });
        
        
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        searchType = 0;
        orderBy = 0;
        lastPage = 0;

        getNotifications();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_beevou_notifications, menu);
		return true;
	}

	private class GetNotifications extends Task {
		private JSONObject json;

		public GetNotifications(Resources resources) {
			super(resources);
			// TODO Auto-generated constructor stub
		}
        
        @Override
        protected Boolean doInBackground(Void... arg0) {
        	try {	
    	    	lastPage = lastPage +1;
    	    	if (lastPage == 1)
    	    		notificationslist.clear();
    	   
    	    	json = UserFunctions.getInstance().getUserNotifications(orderBy, searchType,lastPage);
    		} catch (Exception e) {
            	Log.e("background Error","getUserNotifications");
            	
            }

        return true;
        }
        

        public JSONObject getResult() {
                return json;
        }
    }
    
    


    
    public void loadMoreClick(View arg0) {
        getNotifications();
    }
    
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(BeevouNotifications.this.CONNECTIVITY_SERVICE);
        if (cm == null)
          return false;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
          return false;
        return ni.isConnectedOrConnecting();
      } 
    
    
    private void getNotifications() {
        if (isOnline()) {
        	
        	 mAsyncTaskManager = new AsyncTaskManager(this, this, getString(R.string.getting_notifications_from_beevou_server));
             mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());
             mTask = new  GetNotifications(getResources());
             mAsyncTaskManager.setupTask(mTask);

        } else {
          Toast.makeText(BeevouNotifications.this, R.string.no_connection, Toast.LENGTH_LONG).show();
        }
      }

	@Override
	public void onTaskComplete(Task task) {
		// TODO Auto-generated method stub
		if (!task.isCancelled()) {
            JSONObject json = ((GetNotifications) mTask).getResult();
            processResult(json);
		}
		
	}

	protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Product toegevoegd").setCancelable(true).setPositiveButton("Volgende", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                }
        });
        return builder.create();
    }
	
	
	private void processResult(JSONObject json)
	{
		if (json != null)
		{

		try {
			if (json.has("AuthError") == true) {
				if (json.getString("AuthError").equals("invalid_grant"))
				{	
					Toast.makeText(BeevouNotifications.this, R.string.incorrect_username_password, Toast.LENGTH_LONG).show(); 

					Intent login = new Intent(getApplicationContext(), Login.class);
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Bundle b = new Bundle();
					b.putString("GCM_regID", "");
					login.putExtras(b);
					startActivity(login);
					finish();
				} else {
					Toast.makeText(BeevouNotifications.this, json.getString("AuthError"), Toast.LENGTH_LONG).show();
				}
			} else {
			JSONArray notif = json.getJSONArray("items");
			totalPages = json.getInt("total_pages");
			totalItems = json.getInt("total_items");
			Log.e("total Pages", String.valueOf(totalPages));
			Log.e("total Items", String.valueOf(totalItems));


			if (notif.length() == 0)
			{
				if (mainLayout.getChildCount() == 1)
					mainLayout.addView(noVouchersFound,0);
			} else {
				if (mainLayout.getChildCount() == 2)
					mainLayout.removeView(noVouchersFound);

				for(int i = 0; i < notif.length(); i++){
					JSONObject c = notif.getJSONObject(i);

					String text = c.getString("text");
					String viewed = c.getString("viewed");
					String accepted = c.getString("accepted");  
					String type =  c.getString("type");
					String notification_date = c.getString("created");
					String notificationID = c.getString("id"); 
					
					
					
					SimpleDateFormat name = new SimpleDateFormat("EEEE, dd MMMM yyyy");
					java.util.Date currentDate = new Date();


					Long daysLeft = null;


					java.util.Date notificationDate = null;

					if (!notification_date.equals("null"))
					{

						SimpleDateFormat discountDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try 
						{
							notificationDate = discountDateFormat.parse(notification_date);

						} catch (ParseException ex) {
							ex.printStackTrace();
						}

						notification_date = name.format(notificationDate);
					}


					Long daysAgo = null;

					if (notificationDate != null)
					{

						if (currentDate.getTime() > notificationDate.getTime())
						{
							Calendar calendarDiscountDate = Calendar.getInstance();
							calendarDiscountDate.setTime(notificationDate);

							Calendar calendarCurrentDate = Calendar.getInstance();
							calendarCurrentDate.setTime(currentDate);
							daysAgo =  daysBetween(calendarDiscountDate,calendarCurrentDate);
							daysAgo = daysAgo -1;
						} else {
							daysAgo = Long.valueOf(-1);
						}

					}
					
					HashMap<String, String> map = new HashMap<String, String>();

					map.put("notificationID", notificationID);
					map.put("text", text);
					map.put("viewed", viewed);
					map.put("accepted", accepted);
					map.put("type", type);
					if (daysAgo == 0)
					{
						map.put("daysAgo", getString(R.string.today));
					}
					if (daysAgo == 1)
					{
						map.put("daysAgo", getString(R.string.yesterday));
					}
					if (daysAgo > 1)
					{
						map.put("daysAgo", String.format(getString(R.string._s_days_ago), daysAgo));
					}
				notificationslist.add(map);

				}//for
			}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		}
		
		setadapter();
	}
	
	
	
    private void setadapter()
    {	    	
    	ListAdapter adapter2 = new BeevouNotificationsListItemAdapter(BeevouNotifications.this, notificationslist,  R.layout.beevou_notifications_list_item,
                new String[] {"text","daysAgo"}, new int[] {
        						R.id.notificationText, R.id.daysAgo  });
        if (lastPage < totalPages)
		{		
        	if (lv.getFooterViewsCount() == 0)
        		lv.addFooterView(loadMoreLayout);
		} else {
			if (lv.getFooterViewsCount() > 0)
				lv.removeFooterView(loadMoreLayout);
		}
        
        
        lv.setAdapter(adapter2);

    }

}
