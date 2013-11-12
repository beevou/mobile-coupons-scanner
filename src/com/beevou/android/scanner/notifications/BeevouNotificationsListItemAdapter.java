package com.beevou.android.scanner.notifications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import libraries.BeevouFunctions;
import com.beevou.android.scanner.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BeevouNotificationsListItemAdapter  extends SimpleAdapter  {
	


	    private int[] colors = new int[] { 0x30FFFFFF, 0x30CCCCCC };
	    private LayoutInflater inflater;
	    private List<HashMap<String, String>> items;
	    private Context context;
	    private View view;

	    public BeevouNotificationsListItemAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {

	        super(context, items, resource, from, to);
	        this.context = context;
	        this.inflater = LayoutInflater.from(context);
	        
	        this.items = items;
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	      
	    view = super.getView(position, convertView, parent);
	    
	      String viewed = items.get(position).get("viewed");
	      int type = Integer.valueOf(items.get(position).get("type"));
	      String accepted = items.get(position).get("accepted");
	      final String notificationID = items.get(position).get("notificationID");
	      final LinearLayout buttonsLayout = (LinearLayout)view.findViewById(R.id.buttonsLayout);
	      final TextView notificationText = (TextView)view.findViewById(R.id.notificationText);
	      
	      
  	      if (viewed.equals("y"))
  	      {
  	    	notificationText.setTextColor(Color.LTGRAY);
  	      } else {
  	    	notificationText.setTextColor(Color.BLACK);  
  	      }
  	      
  	      if (type == 10 || type == 30)
  	      {
  	    	  //show accept-reject buttons
  	    	  if (accepted.equals("null"))
  	    	  {
  	    		  buttonsLayout.setVisibility(View.VISIBLE);
  	    	  } else {
  	    		  buttonsLayout.setVisibility(View.GONE);
  	    	  }
  	    	  
  	      } else {
  	    	buttonsLayout.setVisibility(View.GONE);  
  	      }
  	      
  	      
  	    Button acceptButton = (Button)view.findViewById(R.id.acceptButton);
  	     
  	  acceptButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			
			ArrayList<String> passing = new ArrayList<String>();
            passing.add(notificationID);
   	
        	new setAcceptedAsyn_Task().execute(passing);
        	buttonsLayout.setVisibility(View.GONE);
        	notificationText.setTextColor(Color.LTGRAY);
		}

  	    
  	});
  	     
  	  
	    Button rejectButton = (Button)view.findViewById(R.id.rejectButton);
 	     
	    rejectButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			
			ArrayList<String> passing = new ArrayList<String>();
            passing.add(notificationID);
 	
      	new setRejectAsyn_Task().execute(passing);
      	buttonsLayout.setVisibility(View.GONE);
      	notificationText.setTextColor(Color.LTGRAY);
		}

	    
	});
  	      
	    notificationText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				ArrayList<String> passing = new ArrayList<String>();
	            passing.add(notificationID);
	 	
	      	new setViewedAsyn_Task().execute(passing);
	      	notificationText.setTextColor(Color.LTGRAY);
			}

		    
		});
		
	    

	      return view;

	    }

	
	    

	
	    public void accept(View v)
	    {

	    }
	    
	    public void reject(View v)
	    {

	    }

	    
	    private class setAcceptedAsyn_Task extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
	        private final ProgressDialog dialog = new ProgressDialog(context);
	        private JSONObject json;
	        
	        
	        protected void onPreExecute() {
	            this.dialog.setMessage("Accepting...");
	            this.dialog.setCancelable(false);
	            this.dialog.show();
	        }


	        @Override
	        protected void onPostExecute(ArrayList<String> result) {
	        	processResult();
	        	if (this.dialog.isShowing()) {
	                this.dialog.dismiss();
	            }
	        	
	            
	        }
	        

	        
	        
			@Override
			protected ArrayList<String> doInBackground(ArrayList<String>... passing) {
				
				String notID =  passing[0].get(0);

		    	json = BeevouFunctions.getInstance().acceptNotification(notID);
		    	
		    	    	    
			return null;
			}
			
			
	 
			
			private void processResult()
			{
				try {
				
					if (json.getInt("result") == 1)
					{
						LinearLayout buttonsLayout = (LinearLayout)view.findViewById(R.id.buttonsLayout);
	    				buttonsLayout.setVisibility(View.GONE);
						
					} else {
						//TODO: ERROR
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
			}
			
			
	    }
	    
	    
	    private class setRejectAsyn_Task extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
	        private final ProgressDialog dialog = new ProgressDialog(context);
	        private JSONObject json;
	        
	        
	        protected void onPreExecute() {
	            this.dialog.setMessage("Accepting...");
	            this.dialog.setCancelable(false);
	            this.dialog.show();
	        }


	        @Override
	        protected void onPostExecute(ArrayList<String> result) {
	        	processResult();
	        	if (this.dialog.isShowing()) {
	                this.dialog.dismiss();
	            }

	            
	        }
	        

	        
	        
			@Override
			protected ArrayList<String> doInBackground(ArrayList<String>... passing) {
				
				String notID =  passing[0].get(0);

		    	json = BeevouFunctions.getInstance().rejectNotification(notID);
		    	
		    	    	    
			return null;
			}
			
			
	 
			
			private void processResult()
			{
				try {
				
					if (json.getInt("result") == 1)
					{
						LinearLayout buttonsLayout = (LinearLayout)view.findViewById(R.id.buttonsLayout);
	    				buttonsLayout.setVisibility(View.GONE);
						
					} else {
						//TODO: ERROR
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
			}
			
			
	    }
	    
	    
	    private class setViewedAsyn_Task extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
	        private final ProgressDialog dialog = new ProgressDialog(context);
	        private JSONObject json;
	        
	       
	        protected void onPreExecute() {
	            this.dialog.setMessage("Sending...");
	            this.dialog.setCancelable(false);
	            this.dialog.show();
	        }


	        @Override
	        protected void onPostExecute(ArrayList<String> result) {
	        	processResult();
	        	if (this.dialog.isShowing()) {
	                this.dialog.dismiss();
	            }

	            
	        }
	        

	        
	        
			@Override
			protected ArrayList<String> doInBackground(ArrayList<String>... passing) {
				
				String notID =  passing[0].get(0);

		    	json = BeevouFunctions.getInstance().setReadedNotification(notID);
		    	
		    	    	    
			return null;
			}
			
			
	 
			
			private void processResult()
			{
				try {
				
					if (json.getInt("validationCode") == 1)
					{
						
					} else {
						//TODO: ERROR
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
			}
			
			
	    }

}
