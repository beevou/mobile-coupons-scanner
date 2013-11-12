package com.beevou.android.scanner.scan;





import com.beevou.android.scanner.R;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class ScanActivity extends Activity    {
	
	private String emisor;
	private String caducity;
	private String value;
	private String currency;
	private String description;
	private String holder;
	private String pin;
	private Integer validationCode;
	private Integer attempts = 0;
	private String requirements;
	private String comments;
	private Integer voucher_type;
	private Integer cumulative_value;
	private Integer cumulative_target;
	private String nominal;
	private String name;
	private String idVoucher;
	private Integer loyalty_points_accounting;
	private Integer	loyalty_points_per_scan;
	private Float loyalty_money_amount;
	private Integer loyalty_points_per_currency_unit;
	private Float points;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scan);

        Bundle b = getIntent().getExtras();
        validationCode  = b.getInt("validationCode");
       // qrCode =  b.getString("qrCode"); 
        idVoucher = b.getString("idVoucher");
        
        
        
        switch (validationCode) {
        
        //TODO actualizar el documento
        /*
         * 
         * 

         */
        
        
        case -15: {
        	//trying to discount your own voucher for your self
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/1037905-this-voucher-is-yours-you-cant-discount-your-own-vouchers");
            break;
        }
        
        case -14:{
        	//Connected user excluded in template. (Exclude Me = yes)
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/1032903-connected-user-excluded-in-template");
            break;
        }
        
        case -13:{
        	//The connected affiliate is blocked by the issuer
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/1005500");
            break;
        }
        case -12:{
        	//The connected affiliate is not in the affiliates list of the issuer of the voucher
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/1005501");
            break;
        }
        case -11:{
        	//The connected affiliate has no company data
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/1005503");
            break;
        }
        case -10:{
        	//the beneficiary of this voucher have been deleted from the issuer list"
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/1005504");
            break;
        }
        case -9:{
        	//the beneficiary of this voucher have been blocked by the issuer"
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/1005505");
            break;
        }
        case -8:{
        	//this voucher is pending for transfer and cant be read until 
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/1032515-this-voucher-is-being-transfered");
            break;
        }
        
        case -5: { 
        	//this voucher is blocked due to pin violation, the beneficiary must unlock it, login into his private area
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/1005512");
            break;
        }
        case -4: {  
        	//this voucher is blocked by the issuer
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/1005513");
            break;
        }
        case -3: {
        	//this voucher is out of date
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/1005514");
            break;
        }
        case -2: { //this voucher has been discounted before
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/940168-this-voucher-has-been-discounted-before");
            break;
        }
        case -1: { 
        	//you can not discount this voucher, you are not in the affiliates list of the voucher
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/1005541");
            break;
        }
        case 0: { 
        	//the voucher does not exists or is not a valid beevou code
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/1005542");
            break;
        }
        case 1: { 
        	//the voucher exists and can be discounted - could be protected by pin
        	
        	emisor = b.getString("emisor");
			caducity = b.getString("caducity");
			value =	b.getString("value");
			currency = b.getString("currency");
			description = b.getString("description");
			holder = b.getString("beneficiary_name");
			pin = b.getString("pin");
			name = b.getString("name");
			requirements = b.getString("requirements");
			comments = b.getString("comments"); 
			voucher_type = Integer.valueOf(b.getString("voucher_type"));
			nominal = b.getString("nominal");
			if (! b.getString("cumulative_value").equals("null"))
				cumulative_value = Integer.valueOf(b.getString("cumulative_value"));
			if (! b.getString("cumulative_target").equals("null"))
				cumulative_target = Integer.valueOf(b.getString("cumulative_target"));
			
			
			
			
			
			if (! b.getString("loyalty_points_accounting").equals("null"))
				loyalty_points_accounting = Integer.valueOf(b.getString("loyalty_points_accounting"));
			if (! b.getString("loyalty_points_per_scan").equals("null"))
				loyalty_points_per_scan = Integer.valueOf(b.getString("loyalty_points_per_scan"));
			if (! b.getString("loyalty_money_amount").equals("null"))
				loyalty_money_amount = Float.valueOf(b.getString("loyalty_money_amount"));
			if (! b.getString("loyalty_points_per_currency_unit").equals("null"))
				loyalty_points_per_currency_unit = Integer.valueOf(b.getString("loyalty_points_per_currency_unit"));
			if (! b.getString("points").equals("null"))
				points = Float.valueOf(b.getString("points"));
			
			
				if (nominal.equals("y"))
				{
					scanDiscountAlert();
				} else {
					
					if (pin.equals("y"))
					{
						scanPinInterface();
					} else {
						if (voucher_type != 3)
						{
							scanDiscountInterface();
						} else {
							scanLoyaltyInterface();
						}
					}
					
				}
			
				
			
			
			break;
        }
        
        
        case 2: { //this voucher has been discounted before
        	scanErrorInterface(b.getString("error_msg"),"http://support.beevou.net/customer/portal/articles/940168-this-voucher-has-been-discounted-before");
            break;
        }
        
    }
        
        
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
    
    
    public void scanErrorInterface(String message, final String moreInfoURL)
    {
    	LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
    	Button btnBack = (Button) findViewById(R.id.backButton);
    	btnBack.setOnClickListener(new View.OnClickListener() {  //Add a listener for when the button is pressed
            @Override
            public void onClick(View v) {
            	Intent dataIntent = new Intent();
                setResult(-1, dataIntent);
                finish();
            }
    	});
    	
    	LinearLayout LY1 = new LinearLayout(this);
    	LY1.setBackgroundResource(R.drawable.shape_alert_dot_border);
    	LinearLayout.LayoutParams params02 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
        params02.setMargins(10, 10, 10, 10);
        LY1.setOrientation(LinearLayout.VERTICAL);
        LY1.setLayoutParams(params02);
        mainLayout.addView(LY1);
    	
    	ImageView IV = new ImageView(this);
    	IV.setImageResource(R.drawable.error);
    	LY1.addView(IV);
    	
    	TextView  TV = new TextView(this, null, android.R.attr.textAppearanceLarge);
    	TV.setText(message);
    	TV.setTextColor(Color.parseColor("#B94A8A"));
    	TV.setPadding(20, 5, 20, 5);
    	LY1.addView(TV);
    	
    	
    	Button moreInfo = (Button) findViewById(R.id.moreInfoButton);
    	
    	moreInfo.setOnClickListener(new View.OnClickListener() {  //Add a listener for when the button is pressed
            @Override
            public void onClick(View v) {
            	String url = moreInfoURL; // You could have this at the top of the class as a constant, or pass it in as a method variable, if you wish to send to multiple websites
                Intent i = new Intent(Intent.ACTION_VIEW); // Create a new intent - stating you want to 'view something'
                i.setData(Uri.parse(url));  // Add the url data (allowing android to realise you want to open the browser)
                startActivity(i); // Go go go!                     
            }
    	});      
    }
    
    
    
    public void scanErrorInterfaceV2(String message, final String moreInfoURL)
    {
    	Button btnBack = (Button) findViewById(R.id.backButton);
    	btnBack.setOnClickListener(new View.OnClickListener() {  
            @Override
            public void onClick(View v) {
            	finish();	
            }
    	});
    	
    	TextView message1 =  (TextView) findViewById(R.id.message);
    	message1.setText(message);
    	//message1.setTextColor(Color.parseColor("#B94A8A"));
    	Button moreInfo = (Button) findViewById(R.id.moreInfoButton);
    	
    	moreInfo.setOnClickListener(new View.OnClickListener() {  //Add a listener for when the button is pressed
            @Override
            public void onClick(View v) {
            	String url = moreInfoURL; // You could have this at the top of the class as a constant, or pass it in as a method variable, if you wish to send to multiple websites
                Intent i = new Intent(Intent.ACTION_VIEW); // Create a new intent - stating you want to 'view something'
                i.setData(Uri.parse(url));  // Add the url data (allowing android to realise you want to open the browser)
                startActivity(i); // Go go go!                     
            }
    });
    	
    }
    
    

    
    
    public void scanDiscountInterface()
    {
    	Intent discount = new Intent(this, Discount.class);
		Bundle b = new Bundle();
		b.putInt("validationCode", validationCode); 
		b.putString("idVoucher",idVoucher);
		b.putString("name",name);
		b.putString("emisor", emisor);
		b.putString("caducity", caducity);
		b.putString("value", value);
		b.putString("currency", currency);
		b.putString("description", description);
		b.putString("requirements", requirements);
		b.putString("comments", comments);
		b.putString("holder", holder);
		b.putInt("voucher_type", voucher_type);
		if (cumulative_value != null)
		b.putInt("cumulative_value", cumulative_value);
		if (cumulative_target != null)
		b.putInt("cumulative_target", cumulative_target);
		b.putString("pin", pin);
		discount.putExtras(b); 
		startActivity(discount);
		finish();
    }
    
    
    public void scanDiscountAlert()
    {
    	Intent identityAlert = new Intent(this, IdentityAlert.class);
		Bundle b = new Bundle();
		b.putInt("validationCode", validationCode); 
		b.putString("idVoucher",idVoucher);
		b.putString("name",name);
		b.putString("emisor", emisor);
		b.putString("caducity", caducity);
		b.putString("value", value);
		b.putString("currency", currency);
		b.putString("description", description);
		b.putString("requirements", requirements);
		b.putString("comments", comments);
		b.putString("holder", holder);
		b.putInt("voucher_type", voucher_type);
		if (cumulative_value != null)
			b.putInt("cumulative_value", cumulative_value);
			if (cumulative_target != null)
			b.putInt("cumulative_target", cumulative_target);
		b.putString("pin", pin);
		identityAlert.putExtras(b); 
		this.startActivityForResult(identityAlert,1);
		//finish();
    	
    }
    
    public void scanLoyaltyInterface()
    {
    	Intent loyalty = new Intent(this, LoyaltyScan.class);
		Bundle b = new Bundle();
		b.putInt("validationCode", validationCode); 
		b.putString("idVoucher",idVoucher);
		b.putString("name",name);
		b.putString("emisor", emisor);
		b.putString("caducity", caducity);
		b.putString("value", value);
		b.putString("currency", currency);
		b.putString("description", description);
		b.putString("requirements", requirements);
		b.putString("comments", comments);
		b.putString("holder", holder);
		b.putInt("voucher_type", voucher_type);
		if (cumulative_value != null)
		b.putInt("cumulative_value", cumulative_value);
		if (cumulative_target != null)
		b.putInt("cumulative_target", cumulative_target);
		
		if (loyalty_points_accounting != null)
			b.putInt("loyalty_points_accounting", loyalty_points_accounting);
		if (loyalty_points_per_scan != null)
			b.putInt("loyalty_points_per_scan", loyalty_points_per_scan);
		if (loyalty_money_amount != null)
			b.putFloat("loyalty_money_amount", loyalty_money_amount);		
		if (loyalty_points_per_currency_unit != null)
			b.putInt("loyalty_points_per_currency_unit", loyalty_points_per_currency_unit);
		if (points != null)
			b.putFloat("points", points);

		
		
		b.putString("pin", pin);
		loyalty.putExtras(b); 
		startActivity(loyalty);
		finish();
    }
    
    
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	switch (requestCode) {
	    		case 1:
	    		{
	    			if (resultCode == RESULT_OK) {
	    				if (pin.equals("y"))
						{
							scanPinInterface();
						} else {
							
							if (voucher_type != 3)
							{
								scanDiscountInterface();
							} else {
								scanLoyaltyInterface();
							}
						}
	    				break;
	    			}
	    		}
	    		
	    	}
	    	
	    	if (resultCode == RESULT_CANCELED) {
	    		finish();
	    	}
	    	
	    	
	    	
			
	    }
    
    
    private void scanPinInterface()
    {
    	Intent pinIterface = new Intent(this, PinInterface.class);
		Bundle b = new Bundle();
		b.putInt("validationCode", validationCode); 
		b.putString("idVoucher",idVoucher);
		b.putString("name",name);
		b.putString("emisor", emisor);
		b.putString("caducity", caducity);
		b.putString("value", value);
		b.putString("currency", currency);
		b.putString("description", description);
		b.putString("requirements", requirements);
		b.putString("comments", comments);
		b.putString("holder", holder);
		b.putString("pin", pin);
		b.putInt("voucher_type", voucher_type);
		if (cumulative_value != null)
			b.putInt("cumulative_value", cumulative_value);
			if (cumulative_target != null)
			b.putInt("cumulative_target", cumulative_target);
		b.putInt("attempts", 0);
		pinIterface.putExtras(b); 
		startActivity(pinIterface);
		finish(); 
    }
    
    
    public void back(View v)
    {
    	finish();
    }
    
    
    


}
