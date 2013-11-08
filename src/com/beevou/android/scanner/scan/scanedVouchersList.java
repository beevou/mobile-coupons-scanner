package com.beevou.android.scanner.scan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

import libraries.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beevou.android.scanner.R;
import com.beevou.android.scanner.Login;
import com.beevou.android.scanner.taskmanager.AsyncTaskManager;
import com.beevou.android.scanner.taskmanager.OnTaskCompleteListener;
import com.beevou.android.scanner.taskmanager.Task;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class scanedVouchersList extends Activity implements 
OnTaskCompleteListener {
	
	final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
	
	private ArrayList<HashMap<String, String>> vouchersList = new ArrayList<HashMap<String, String>>();
	private Context mContext;
	private static final String TAG = "DialogDemo";
	private int orderBy = 0;
	private int searchType = 0;
	private int totalPages = 0;
	private int totalItems = 0;  
	private int lastPage = 0;
	private LinearLayout loadMoreLayout;
	private LinearLayout mainLayout;
	private TextView noVouchersFound;
	private AsyncTaskManager mAsyncTaskManager;
	private Task mTask;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        setContentView(R.layout.activity_scanedvoucherslist);
        
        
        vouchersList.clear();
        
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
                // Starting a new async task
            	getVouchers();
            }
        });
        
        
        noVouchersFound = new TextView(this);
        noVouchersFound.setTextAppearance(this, android.R.attr.textAppearanceLarge);
        noVouchersFound.setBackgroundColor(Color.WHITE);
        noVouchersFound.setTextColor(Color.GRAY);
        noVouchersFound.setTextSize(24);
        noVouchersFound.setText(R.string.no_vouchers_found_);
        
        
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        
        
        lastPage = 0;
        
        
        
        getVouchers();

        
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

    public void showOrderByButtonClick(View v) {
    	 	        Log.i(TAG, "show OrderBy Dialog ButtonClick");
    	        AlertDialog.Builder builder =
    	            new AlertDialog.Builder(mContext);
    	        builder.setTitle(R.string.order_vouchers_by);
    	        
    	        String scan_date = getString(R.string.scan_date);
    	        String issuer = getString(R.string.issuer);
    	        String nominal_value = getString(R.string.nominal_value);
    	        String affiliate_value = getString(R.string.affiliate_value);
    	        
    	        final CharSequence[] choiceList =
    	        {scan_date, issuer , nominal_value, affiliate_value};
    	        int selected = orderBy; // -1 does not select anything
    	        builder.setSingleChoiceItems(
    	                choiceList,
    	                selected,
    	                new DialogInterface.OnClickListener() {
    	            @Override
    	            public void onClick(DialogInterface dialog, int which) {
    	            	switch (which) {
    	            	case 0: {
    	            		orderBy = 0;
    	            		break;
    	            	}
    	            	case 1: {
    	            		orderBy = 1;
    	            		break;
    	            	}
    	            	case 2: {
    	            		orderBy = 2;
    	            		break;
    	            	}
    	            	case 3: {
    	            		orderBy = 3;
    	            		break;
    	            	}
    	            	}
    	            	lastPage = 0;
    	            	getVouchers();
    	            	dialog.cancel();

    	            }
    	        });
    	        AlertDialog alert = builder.create();
    	        alert.show();
    	    }

    	  
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	if (requestCode == 1) {

    		if(resultCode == RESULT_OK){

    				String result=data.getStringExtra("idvoucher");
    			
    			
    			for (Integer i = 0; i < vouchersList.size();i ++)
    			{
    					HashMap<String, String> record = (HashMap<String, String>) vouchersList.get(i);
    					if (record.get("idvoucher").equals(result))
    					{
    						vouchersList.remove(i);
    						break;
    					}
    			}
    			
    			getVouchers();

    		}
 
    		if (resultCode == RESULT_CANCELED) {


    		}
    	}
    }
    
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(scanedVouchersList.this.CONNECTIVITY_SERVICE);
        if (cm == null)
          return false;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
          return false;
        return ni.isConnectedOrConnecting();
      } 
    
    private void getVouchers()
    {
        if (isOnline()) {
    	
    	mAsyncTaskManager = new AsyncTaskManager(this, this, getString(R.string.getting_vouchers_from_server_));
        mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());
        mTask = new  GetScannedVouchers(getResources());
        mAsyncTaskManager.setupTask(mTask);
        } else {
            Toast.makeText(scanedVouchersList.this, R.string.no_network_connection, Toast.LENGTH_LONG).show();
          }
        
    }
    
    
    


    private class GetScannedVouchers extends Task {

    	public GetScannedVouchers(Resources resources) {
			super(resources);
			// TODO Auto-generated constructor stub
		}

		private JSONObject json;
        
        
        
        @Override
        protected Boolean doInBackground(Void... arg0) {
	    	
        	lastPage = lastPage +1;
	    	if (lastPage == 1)
	    		vouchersList.clear();
	    	json = UserFunctions.getInstance().getScanedVouchers(orderBy, searchType,lastPage);
	    	    	    
		return true;
		}
		
        public JSONObject getResult() {
            return json;
        }
		
		
		
    }


	@Override
	public void onTaskComplete(Task task) {
		if (!task.isCancelled()) {
    		JSONObject json = ((GetScannedVouchers) mTask).getResult();
    		processResult(json);
		}
	}
	
	
	private void processResult(JSONObject json)
	{
    	try {
    		if (json.has("AuthError") == true) {
				if (json.getString("AuthError").equals("invalid_grant"))
				{	
					Toast.makeText(scanedVouchersList.this, R.string.incorrect_username_password, Toast.LENGTH_LONG).show(); 

					Intent login = new Intent(getApplicationContext(), Login.class);
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Bundle b = new Bundle();
					b.putString("GCM_regID", "");
					login.putExtras(b);
					startActivity(login);
					finish();
				} else {
					Toast.makeText(scanedVouchersList.this, json.getString("AuthError"), Toast.LENGTH_LONG).show();
				}
			} else {
    		JSONArray vouchers = json.getJSONArray("items");
	    	totalPages = json.getInt("total_pages");
	    	totalItems = json.getInt("total_items");
	    	Log.e("total Pages", String.valueOf(totalPages));
	    	Log.e("total Items", String.valueOf(totalItems));
	    	Log.e("total vouchers", String.valueOf(vouchers.length()));
	    	
	    	
	    	if (vouchers.length() == 0)
			{
				if (mainLayout.getChildCount() == 1)
					mainLayout.addView(noVouchersFound,0);
			} else {
				if (mainLayout.getChildCount() == 2)
					mainLayout.removeView(noVouchersFound);

    	    for(int i = 0; i < vouchers.length(); i++){
    	    	
    	    	JSONObject voucherRead = vouchers.getJSONObject(i).getJSONObject("VouchersRead");
    	        JSONObject c = vouchers.getJSONObject(i).getJSONObject("Voucher");
 
    	        String voucherReadID = voucherRead.getString("id");
    	        String cumulativeStatus = voucherRead.getString("cumulative_status");
    	        String voucherType = c.getString("voucher_template_voucher_type");
    	        String cumulativeTotal = "";
    	        String cumulativeStatusString = "";
    	        if (!voucherType.equals("0"))
    	        {
    	        	cumulativeTotal = c.getString("voucher_template_cumulative_value");
    	            cumulativeStatusString = String.format("%s "+ getString(R.string.read_of_s), cumulativeStatus,cumulativeTotal);
    	        }
    	        

    	        String voucherStatus = c.getString("status");
    	        
    	        String description = ""; 
    	        	if (!c.isNull("voucher_template_description")) 
    	        	{
    	        		description = c.getString("voucher_template_description");
    	        	}
    	        String value = "";
    	        	if (!c.isNull("value"))
    	        	{
    	        		value = c.getString("value");
    	        	}
    	        String stringValue = "";
    	        String currency = "";
    	        		if (!c.isNull("currency"))
        	        	{
        	        		currency = c.getString("currency");
        	        		
        	        		if (currency.equals("$"))
        	        			stringValue = currency+value;
        	        		else
        	        			stringValue = value + " "+currency;
        	        	}	

    	        String emisor = c.getString("issuer_name");
    	        
    	        String discount_date = "";
    	        
    	        if (voucherStatus.equals("2"))
    	        {
    	        	discount_date = c.getString("discount_date");
    	        } else {
    	        	discount_date = voucherRead.getString("discount_date");
    	        }
    	        
    	      
    	        String idvoucher = c.getString("id");
    	        String voucher_template_type = c.getString("voucher_template_type");
    	        String voucher_template_name = c.getString("voucher_template_name");
    	        
    	        
    	        
    	        String affiliate_value = "";
	        	if (!c.isNull("voucher_template_affiliate_value"))
	        	{
	        		affiliate_value = c.getString("voucher_template_affiliate_value");
	        	}
	        	String stringAffiliateValue = "";
	        	String affiliate_currency = "";
	        		if (!c.isNull("voucher_template_affiliate_currency"))
    	        	{
	        			affiliate_currency = c.getString("voucher_template_affiliate_currency");
    	        		
    	        		if (affiliate_currency.equals("$"))
    	        			stringAffiliateValue = affiliate_currency+affiliate_value;
    	        		else
    	        			stringAffiliateValue = affiliate_value + " "+affiliate_currency;
    	        	}
    	        
    	        
    	        String issuerID = c.getString("user_id");
    	        String voucher_template_image3 = c.getString("voucher_template_image3");

    	        
    	        SimpleDateFormat name = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    	        
    	        
 
    	        if (discount_date != null)
    	        	if (!discount_date.equals("null") )
    	        	{
    	        		java.util.Date discountDate = null;
    	        		SimpleDateFormat discountDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	        		try 
    	        		{
    	        			discountDate = discountDateFormat.parse(discount_date);

    	        		} catch (ParseException ex) {
    	        			ex.printStackTrace();
    	        		}

    	        		discount_date = name.format(discountDate);
    	        	}
     	        // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
                // adding each child node to HashMap key => value
                map.put("idvoucher", idvoucher);
                map.put("description", description);
                map.put("value", value);
                map.put("currency", currency);
                map.put("stringValue", stringValue);
                map.put("emisor", emisor);
                map.put("discount_date", discount_date);
                map.put("voucher_template_type", voucher_template_type);
                map.put("voucher_template_name", voucher_template_name);
                map.put("issuerID", issuerID);
                map.put("voucher_template_image3",voucher_template_image3);
                map.put("voucherReadID",voucherReadID);
                map.put("cumulativeStatus",cumulativeStatus);
                map.put("voucherStatus",voucherStatus);
                map.put("cumulativeStatusString",cumulativeStatusString);
                map.put("voucherType",voucherType);
                
                map.put("affiliate_value",affiliate_value);
	        	map.put("stringAffiliateValue",stringAffiliateValue);
	        	map.put("affiliate_currency",affiliate_currency);

                vouchersList.add(map);
    	 
    	    }//for
		}   
			}
    	} catch (JSONException e) {
    	    e.printStackTrace();
    	}    	    

    	setadapter();
	}
	
	private void setadapter()
    {
    	ListAdapter adapter = new scanedVoucherListItemAdapter(scanedVouchersList.this, vouchersList,
                R.layout.scanned_voucher_list_item,
                new String[] { "voucher_template_name", "stringAffiliateValue", "discount_date", "description", "emisor", "cumulativeStatusString"}, new int[] {
        						R.id.title, R.id.value,R.id.discountDate, R.id.description, R.id.issuer, R.id.cumulativeStatus  });
    	
    	ListView lv = (ListView) findViewById(R.id.list);
    	
    	if (lastPage < totalPages)
		{		
        	if (lv.getFooterViewsCount() == 0)
        		lv.addFooterView(loadMoreLayout);
		} else {
			lv.removeFooterView(loadMoreLayout);
		}
    	
        lv.setAdapter(adapter);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
       	 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	Intent in = new Intent(getApplicationContext(), ScannedVoucher.class);
            	

            	
            	Bundle b = new Bundle();
        		b.putString("idvoucher",vouchersList.get(position).get("idvoucher"));
        		b.putString("emisor", vouchersList.get(position).get("emisor"));
        		b.putString("value", vouchersList.get(position).get("value"));
        		b.putString("stringValue", vouchersList.get(position).get("stringValue"));
        		b.putString("currency", vouchersList.get(position).get("currency"));
        		b.putString("description", vouchersList.get(position).get("description"));
        		b.putString("discounted_on", vouchersList.get(position).get("discounted_on"));
        		b.putString("discounted_on_id", vouchersList.get(position).get("discounted_on_id"));
        		b.putString("discount_date", vouchersList.get(position).get("discount_date"));
        		b.putString("issuerID", vouchersList.get(position).get("issuerID"));
        		b.putString("voucher_template_name", vouchersList.get(position).get("voucher_template_name"));
        		b.putString("voucherReadID", vouchersList.get(position).get("voucherReadID"));
        		b.putString("cumulativeStatus", vouchersList.get(position).get("cumulativeStatus"));
        		b.putString("voucherStatus", vouchersList.get(position).get("voucherStatus"));
        		b.putString("cumulativeStatusString", vouchersList.get(position).get("cumulativeStatusString"));
        		b.putString("voucherType", vouchersList.get(position).get("voucherType"));
        		b.putString("affiliate_value", vouchersList.get(position).get("affiliate_value"));
        		b.putString("stringAffiliateValue", vouchersList.get(position).get("stringAffiliateValue"));
        		b.putString("affiliate_currency", vouchersList.get(position).get("affiliate_currency"));
        		
        		in.putExtras(b);
        		
                startActivityForResult(in,1);

            }

			
        }); 
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
    

