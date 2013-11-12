package com.beevou.android.scanner.scan;

import java.util.HashMap;

import java.util.List;

import libraries.BeevouFunctions;

import com.beevou.android.scanner.R;
import com.beevou.android.scanner.R.id;
import com.beevou.android.scanner.AsyncImageLoader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class scanedVoucherListItemAdapter  extends SimpleAdapter {
	


	    private int[] colors = new int[] { 0x30FFFFFF, 0x30CCCCCC };
	    private LayoutInflater inflater;
	    private List<HashMap<String, String>> items;
	    private AsyncImageLoader asyncImageLoader;
	     
 
	    public scanedVoucherListItemAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {

	        super(context, items, resource, from, to);
	        this.inflater = LayoutInflater.from(context);
	        this.items = items;
	        asyncImageLoader = new AsyncImageLoader();

	    }

	 

	    @Override

	    public View getView(int position, View convertView, ViewGroup parent) {

	    	
	    	Integer voucherTemplateType = Integer.valueOf(items.get(position).get("voucher_template_type"));

  	        View view = super.getView(position, convertView, parent);
	      
	      /*
	       * 
	       * read codes:

    		-1 - Deleted by issuer
    		-4 - Block by issuer
    		-5 - Block due to pin violation
    		-8 - Transfer pending 
     		0 - Rejected 
     		1 - Pending 
     		2 - Discounted	       */
  	        
  	       
  	      String voucherType = items.get(position).get("voucherType");
  	      if (voucherType.equals("0"))
  	      {
  	    	TextView cumulativeStatus = (TextView)view.findViewById(R.id.cumulativeStatus); 
  	    	cumulativeStatus.setVisibility(View.INVISIBLE);
  	      } else {
  	    	TextView cumulativeStatus = (TextView)view.findViewById(R.id.cumulativeStatus); 
  	    	cumulativeStatus.setVisibility(View.VISIBLE);  
  	      } 
  	      
  	      
	      
	      TextView statusText = (TextView)view.findViewById(R.id.statusText);
	      int alpha = 80;
    	  statusText.setTextColor(Color.argb(alpha, 255, 0, 0));
    	  String voucherStatus = items.get(position).get("voucherStatus");
    	  
    	  if (voucherStatus.equals("2"))
    	  {
    		  statusText.setText(R.string.discounted);
    	  } else {
    		  statusText.setText(R.string.readed);
    	  }
    	  
          
          
          final ImageView theImage =   (ImageView) view.findViewById(R.id.imageView1);
          
          theImage.setImageResource(R.drawable.noimagevoucher);
          
          if (items.get(position).get("voucher_template_image3") != null)
	      {
	    	  if (!items.get(position).get("voucher_template_image3").equals("null"))
	    	  {
	    		  String issuerID = items.get(position).get("issuerID"); 
	    		  asyncImageLoader.fetchDrawableOnThread("https://beevou.net/users/"+issuerID+"/"+items.get(position).get("voucher_template_image3"), theImage);
	    	  
	    	  } else {
	    		  theImage.setImageResource(R.drawable.noimagevoucher);
	    	  }
	      } else {
	    	  theImage.setImageResource(R.drawable.noimagevoucher);
	      }
          

          
	      return view;
	    }

	

	
	

}
