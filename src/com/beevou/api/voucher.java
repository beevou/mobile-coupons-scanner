package com.beevou.api;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import libraries.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

public class voucher implements Serializable {

	
	private String idvoucher;
	private String voucherName;
	private String voucherTitle;
	private String description;
	private String value;
	private String currency;
	private String stringValue;
	private Date caducity;
	private String caducityString;
	private String issuerName;
	private String issuerID;
	private int status;
	private String discountedOn;
	private String discountedOnId;
	private Date discountDate;
	private String discountDateString;
	private String requirements;
	private Boolean canBeTransfered = false;
	private String qrCode;
	private Date transferDate;
	private String transferDateString;
	private Date startDate;
	private String startDateString;
	private String pinCode;
	private String voucherIcon;
	private String voucherImage;
	private String voucherBackground;
	private int cumulativeType = 0;
	private int cumulativeValue = 1;
	private int cumulativeStatus = 0;
	private int defaultCodeType = 1;
	private String userTo;
	private String transferTo;
	static SimpleDateFormat dateOutputFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
	static SimpleDateFormat dateIncomingFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String connectedUserID;
	private String beneficiaryName;
	private String transferToName;
	private int defaultBarcodeType;
	
	
	public voucher(String connectedUserID)
	{
		this.connectedUserID = connectedUserID;
	}
	
	public voucher(JSONObject json,String connectedUserID)
	{
		this.connectedUserID = connectedUserID;
	  
	  try{
		
		if (!json.getString("qr_code").equals("null") &&!json.getString("qr_code").equals("") )
		this.qrCode = json.getString("qr_code");
		
		
		if (!json.getString("voucher_template_name").equals("null") &&!json.getString("voucher_template_name").equals("") )
		{
			this.voucherName = json.getString("voucher_template_name");
			if (!json.getString("issuer_name").equals("null") &&!json.getString("issuer_name").equals("") )
			{
				this.voucherTitle = json.getString("voucher_template_name") + " ["+json.getString("issuer_name")+"]";
			} else {
				this.voucherTitle = json.getString("voucher_template_name");	
			}
		} 
		
		if (!json.getString("voucher_template_description").equals("null") &&!json.getString("voucher_template_description").equals("") )
			this.description = json.getString("voucher_template_description");


		if (!json.getString("value").equals("null") &&!json.getString("value").equals("") )
		{
			this.value = json.getString("value");
			if (!json.isNull("currency_symbol"))
			{
				currency = json.getString("currency_symbol");

				if (currency.equals("$"))
					stringValue = currency+value;
				else
					stringValue = value + " "+currency;
			} 
		} else {
			stringValue = "";
		}

		 	
		
		
		
		if (!json.getString("end_date").equals("null") &&!json.getString("end_date").equals("") )
		{
			String caducitySTR = json.getString("end_date");

			try 
			{
				caducity = dateIncomingFormat.parse(caducitySTR+" 23:59:59");

			} catch (ParseException ex) {
				ex.printStackTrace();
			}


			
			setCaducityString(dateOutputFormat.format(caducity));
			
		} 
		
		
		if (!json.getString("issuer_name").equals("null") &&!json.getString("issuer_name").equals("") )
			issuerName = json.getString("issuer_name");
		if (!json.getString("beneficiary_name").equals("null") &&!json.getString("beneficiary_name").equals("") )
			setBeneficiaryName(json.getString("beneficiary_name"));
		
		
		if (!json.getString("status").equals("null") &&!json.getString("status").equals("") )
			status = json.getInt("status");
		
		if (!json.getString("discount_on").equals("null") &&!json.getString("discount_on").equals("") )
			discountedOn = json.getString("discount_on");
		
		if (!json.getString("discount_on_id").equals("null") &&!json.getString("discount_on_id").equals("") )
			discountedOnId = json.getString("discount_on_id");
		
		if (!json.getString("user_to").equals("null") &&!json.getString("user_to").equals("") )
			setUserTo(json.getString("user_to"));
		
		if (!json.getString("transfer_to").equals("null") &&!json.getString("transfer_to").equals("") )
			setTransferTo(json.getString("transfer_to"));
		
		if (!json.getString("transfer_to_name").equals("null") &&!json.getString("transfer_to_name").equals("") )
			setTransferToName(json.getString("transfer_to_name"));
		if (!json.getString("voucher_template_barcode_type").equals("null") &&!json.getString("voucher_template_barcode_type").equals("") )
			setDefaultBarcodeType(json.getInt("voucher_template_barcode_type"));
		
		
		if (!json.getString("discount_date").equals("null") &&!json.getString("discount_date").equals("") )
		{
				String discountDateSTR = json.getString("discount_date");
		
				try 
				{
					discountDate = dateIncomingFormat.parse(discountDateSTR);

				} catch (ParseException ex) {
					ex.printStackTrace();
				}

				setDiscountDateString(dateOutputFormat.format(discountDate));
		
		}
		
		if (!json.getString("voucher_template_instructions").equals("null") &&!json.getString("voucher_template_instructions").equals("") )
		requirements = json.getString("voucher_template_instructions");
		
		if (!json.getString("voucher_template_can_be_transferred").equals("null") &&!json.getString("voucher_template_can_be_transferred").equals("") )
		{
			String can_be_transfered = json.getString("voucher_template_can_be_transferred");
			if (can_be_transfered.equals("y"))
			{
				canBeTransfered = true;
			} else {
				canBeTransfered = false;
			}
		}
		
		if (!json.getString("id").equals("null") &&!json.getString("id").equals("") )
		idvoucher = json.getString("id");
		
		if (!json.getString("start_date").equals("null") &&!json.getString("start_date").equals("") )
		{
				String startDateSTR = json.getString("start_date");
				
				try 
				{
					startDate = dateIncomingFormat.parse(startDateSTR);

				} catch (ParseException ex) {
					ex.printStackTrace();
				}

				setStartDateString(dateOutputFormat.format(startDate));
		}
		
		if (!json.getString("transfer_date").equals("null") &&!json.getString("transfer_date").equals("") )
			{
					String transferDateSTR = json.getString("transfer_date");
					
					try 
					{
						transferDate = dateIncomingFormat.parse(transferDateSTR);

					} catch (ParseException ex) {
						ex.printStackTrace();
					}

					setTransferDateString(dateOutputFormat.format(transferDate));			
			
			
			}
		
		if (!json.getString("pin_code").equals("null") &&!json.getString("pin_code").equals("") )
			pinCode = json.getString("pin_code");
		
		if (!json.getString("voucher_template_image1").equals("null") &&!json.getString("voucher_template_image1").equals("") )
			voucherBackground = json.getString("voucher_template_image1");
		
		if (!json.getString("voucher_template_image2").equals("null") &&!json.getString("voucher_template_image2").equals("") )
			voucherImage = json.getString("voucher_template_image2");
		
		if (!json.getString("voucher_template_image3").equals("null") &&!json.getString("voucher_template_image3").equals("") )
			voucherIcon = json.getString("voucher_template_image3");
		
		if (!json.getString("user_id").equals("null") &&!json.getString("user_id").equals("") )
			setIssuerID(json.getString("user_id"));


		if (!json.getString("voucher_template_cumulative_type").equals("null") &&!json.getString("voucher_template_cumulative_type").equals("") )
			cumulativeType = json.getInt("voucher_template_cumulative_type"); // (0 one use only, 1 cumulative, 2 multiple)
		if (!json.getString("voucher_template_cumulative_value").equals("null") &&!json.getString("voucher_template_cumulative_value").equals("") )
			cumulativeValue = json.getInt("voucher_template_cumulative_value"); // (total number of uses)
		if (!json.getString("cumulative_status").equals("null") &&!json.getString("cumulative_status").equals("") )
			cumulativeStatus = json.getInt("cumulative_status");// (actual number of uses)
		if (!json.getString("voucher_template_barcode_type").equals("null") &&!json.getString("voucher_template_barcode_type").equals("") )
			defaultCodeType = json.getInt("voucher_template_barcode_type"); //default code bar type





	   } catch (JSONException e) {
			e.printStackTrace();
	   }



	}
	
	
	public Long getDaysLeft()
	{
		java.util.Date currentDate = new Date();

		Long daysLeft = null;

		if (caducity != null)
		{

			if (currentDate.getTime() < caducity.getTime())
			{
				Calendar calendarCaducityDate = Calendar.getInstance();
				calendarCaducityDate.setTime(caducity);

				Calendar calendarCurrentDate = Calendar.getInstance();
				calendarCurrentDate.setTime(currentDate);
				daysLeft =  daysBetween(calendarCurrentDate,calendarCaducityDate);
				daysLeft = daysLeft -1;

			} else {
				daysLeft = Long.valueOf(-1);
			}

		}
		
		return daysLeft;
	}
	
	public Long getDiscountedDaysAgo()
	{
		java.util.Date currentDate = new Date();
		
		Long daysAgo = null;

		if (discountDate != null)
		{

			if (currentDate.getTime() > discountDate.getTime())
			{
				Calendar calendarDiscountDate = Calendar.getInstance();
				calendarDiscountDate.setTime(discountDate);

				Calendar calendarCurrentDate = Calendar.getInstance();
				calendarCurrentDate.setTime(currentDate);
				daysAgo =  daysBetween(calendarDiscountDate,calendarCurrentDate);
				daysAgo = daysAgo -1;
			} else {
				daysAgo = Long.valueOf(-1);
			}

		}
		
		return daysAgo;
	}
	
	public Long getExpiredDaysAgo()
	{
		java.util.Date currentDate = new Date();
		
		Long daysAgo = null;

		if (caducity != null)
		{

			if (currentDate.getTime() > caducity.getTime())
			{
				Calendar calendarDiscountDate = Calendar.getInstance();
				calendarDiscountDate.setTime(caducity);

				Calendar calendarCurrentDate = Calendar.getInstance();
				calendarCurrentDate.setTime(currentDate);
				daysAgo =  daysBetween(calendarDiscountDate,calendarCurrentDate);
				daysAgo = daysAgo -1;
			} else {
				daysAgo = Long.valueOf(-1);
			}

		}
		
		return daysAgo;
	}
	
	
    private static long daysBetween(Calendar startDate, Calendar endDate) {  
  	  Calendar date = (Calendar) startDate.clone();  
  	  long daysBetween = 0;  
  	  while (date.before(endDate)) {  
  	    date.add(Calendar.DAY_OF_MONTH, 1);  
  	    daysBetween++;  
  	  }  
  	  return daysBetween;  
  	}  

	public String getIdvoucher() {
		return idvoucher;
	}

	public void setIdvoucher(String idvoucher) {
		this.idvoucher = idvoucher;
	}

	public String getVoucherName() {
		return voucherName;
	}

	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}

	public String getVoucherTitle() {
		return voucherTitle;
	}

	public void setVoucherTitle(String voucherTitle) {
		this.voucherTitle = voucherTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Date getCaducity() {
		return caducity;
	}

	public void setCaducity(Date caducity) {
		this.caducity = caducity;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDiscountedOn() {
		return discountedOn;
	}

	public void setDiscountedOn(String discountedOn) {
		this.discountedOn = discountedOn;
	}

	public String getDiscountedOnId() {
		return discountedOnId;
	}

	public void setDiscountedOnId(String discountedOnId) {
		this.discountedOnId = discountedOnId;
	}

	public Date getDiscountDate() {
		return discountDate;
	}

	public void setDiscountDate(Date discountDate) {
		this.discountDate = discountDate;
	}


	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

	public Boolean getCanBeTransfered() {
		return canBeTransfered;
	}

	public void setCanBeTransfered(Boolean canBeTransfered) {
		this.canBeTransfered = canBeTransfered;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getPinCode() {
		return pinCode;
	}

	public JSONObject setPinCode(String pinCode) {
		JSONObject json = UserFunctions.getInstance().savePin(this.idvoucher,pinCode);
		this.pinCode = pinCode;
		
		return json;
	}

	public String getVoucherIcon() {
		return voucherIcon;
	}

	public void setVoucherIcon(String voucherIcon) {
		this.voucherIcon = voucherIcon;
	}

	public String getVoucherImage() {
		return voucherImage;
	}

	public void setVoucherImage(String voucherImage) {
		this.voucherImage = voucherImage;
	}

	public String getVoucherBackground() {
		return voucherBackground;
	}

	public void setVoucherBackground(String voucherBackground) {
		this.voucherBackground = voucherBackground;
	}

	public int getCumulativeType() {
		return cumulativeType;
	}

	public void setCumulativeType(int cumulativeType) {
		this.cumulativeType = cumulativeType;
	}

	public int getCumulativeValue() {
		return cumulativeValue;
	}

	public void setCumulativeValue(int cumulativeValue) {
		this.cumulativeValue = cumulativeValue;
	}

	public int getCumulativeStatus() {
		return cumulativeStatus;
	}

	public void setCumulativeStatus(int cumulativeStatus) {
		this.cumulativeStatus = cumulativeStatus;
	}

	public int getDefaultCodeType() {
		return defaultCodeType;
	}

	public void setDefaultCodeType(int defaultCodeType) {
		this.defaultCodeType = defaultCodeType;
	}


	public String getIssuerName() {
		return issuerName;
	}


	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}


	public String getIssuerID() {
		return issuerID;
	}


	public void setIssuerID(String issuerID) {
		this.issuerID = issuerID;
	}


	public String getCaducityString() {
		return caducityString;
	}


	public void setCaducityString(String caducityString) {
		this.caducityString = caducityString;
	}


	public String getDiscountDateString() {
		return discountDateString;
	}


	public void setDiscountDateString(String discountDateString) {
		this.discountDateString = discountDateString;
	}


	public String getTransferDateString() {
		return transferDateString;
	}


	public void setTransferDateString(String transferDateString) {
		this.transferDateString = transferDateString;
	}


	public String getStartDateString() {
		return startDateString;
	}


	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
	}
	
    public JSONObject clearPinCode()
    {
    	JSONObject json = UserFunctions.getInstance().removePin(this.idvoucher);
    	this.pinCode = null;
    	return json;
    }
    
    
    public JSONObject delete()
    {
    	JSONObject json = UserFunctions.getInstance().deleteVoucher(this.idvoucher);
    	return json;
    }
    
    public JSONObject transfer(String email)
    {
    	JSONObject json = UserFunctions.getInstance().transferVoucher(this.idvoucher, email);
    	return json;
    }
    
    public JSONObject restoreVoucherTransfer()
    {
    	JSONObject json = UserFunctions.getInstance().restoreVoucherTransfer(this.idvoucher);
    	return json;
    }
    
    public JSONObject rejectVoucherTransfer()
    {
    	JSONObject json = UserFunctions.getInstance().rejectVoucherTransfer(this.idvoucher);
    	return json;
    }
    
    public JSONObject acceptVoucherTransfer()
    {
    	JSONObject json = UserFunctions.getInstance().acceptVoucherTransfer(this.idvoucher);
    	return json;
    }
    
    public String getVoucherInfo()
    {
    	String info = UserFunctions.getInstance().viewVoucher(this.idvoucher);
    	return info;
    }
    
    public void copyFrom(voucher otherVoucher)
    {
    	idvoucher = otherVoucher.getIdvoucher();
    	voucherName = otherVoucher.getVoucherName();
    	voucherTitle  = otherVoucher.getVoucherTitle();
    	description = otherVoucher.getDescription();
    	value = otherVoucher.getValue();
    	currency = otherVoucher.getCurrency();
    	stringValue = otherVoucher.getStringValue();
    	caducity = otherVoucher.getCaducity();
    	caducityString = otherVoucher.getCaducityString();
    	issuerName = otherVoucher.getIssuerName();
    	issuerID = otherVoucher.getIssuerID();
    	status = otherVoucher.getStatus();
    	discountedOn = otherVoucher.getDiscountedOn();
    	discountedOnId = otherVoucher.getDiscountedOnId();
    	discountDate = otherVoucher.getDiscountDate();
    	discountDateString = otherVoucher.getDiscountDateString();
    	requirements = otherVoucher.getRequirements();
    	canBeTransfered = otherVoucher.getCanBeTransfered();
    	qrCode = otherVoucher.getQrCode();
    	transferDate = otherVoucher.getTransferDate();
    	transferDateString = otherVoucher.getTransferDateString();
    	startDate = otherVoucher.getStartDate();
    	startDateString = otherVoucher.getStartDateString();
    	pinCode = otherVoucher.getPinCode();
    	voucherIcon = otherVoucher.getVoucherIcon();
    	voucherImage = otherVoucher.getVoucherImage();
    	voucherBackground = otherVoucher.getVoucherBackground();
    	cumulativeType = otherVoucher.getCumulativeType();
    	cumulativeValue = otherVoucher.getCumulativeValue();
    	cumulativeStatus = otherVoucher.getCumulativeStatus();
    	defaultCodeType  = otherVoucher.getDefaultCodeType();
    }


	public String getUserTo() {
		return userTo;
	}


	public void setUserTo(String userTo) {
		this.userTo = userTo;
	}


	public String getTransferTo() {
		return transferTo;
	}


	public void setTransferTo(String transferTo) {
		this.transferTo = transferTo;
	}
	
	
	private Long getDaysFrom(Date theDate)
	{
		java.util.Date currentDate = new Date();
		
		Long days = null;

		if (theDate != null)
		{
				Calendar calendarDate = Calendar.getInstance();
				calendarDate.setTime(theDate);
				Calendar calendarCurrentDate = Calendar.getInstance();
				calendarCurrentDate.setTime(currentDate);
				days =  daysBetween(calendarDate,calendarCurrentDate);
				
				if (calendarCurrentDate.after(calendarDate))
				{
					days = days*(-1);
				}
		}
		
		
		
		return days;
	}
	
	
	public int getTransferStatus(){
		
		int result = 0;
		
		
		if ((!this.userTo.equals(this.transferTo)) && (connectedUserID.equals(this.userTo)))
		{
			//outgoing transfer
			result = 0;
		}
		
		if ((!this.userTo.equals(this.transferTo)) && (connectedUserID.equals(this.transferTo)))
		{
			//incoming transfer
			result = 1;
		}
		
		if (this.transferDate != null)
			if ((!this.userTo.equals(this.transferTo)) && (connectedUserID.equals(this.userTo)) && (getDaysFrom(this.transferDate) <= -2))
				{
				//restorable transfer
				result = 2;
				}
		
		return result;
		
		
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getTransferToName() {
		return transferToName;
	}

	public void setTransferToName(String transferToName) {
		this.transferToName = transferToName;
	}

	public int getDefaultBarcodeType() {
		return defaultBarcodeType;
	}

	public void setDefaultBarcodeType(int defaultBarcodeType) {
		this.defaultBarcodeType = defaultBarcodeType;
	}
    
	
	
}
