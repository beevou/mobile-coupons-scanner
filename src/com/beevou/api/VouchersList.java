package com.beevou.api;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import libraries.UserFunctions;

public class VouchersList extends ArrayList<voucher> 
{

	private int orderBy;
	private int searchType;
	private int searchFor;
	private int totalPages;
	private int totalItems;
	private String connectedUserID;

	
	public void getVouchers(int searchFor,int searchType,int orderBy, int lastPage)
	{
		
		if (this.orderBy != orderBy)
		{
			this.orderBy = orderBy;
			this.clear();
		}
		
		if (this.searchType != searchType)
		{
			this.searchType = searchType;
			this.clear();
		}
		
		
		
		JSONObject json = UserFunctions.getInstance().getMyVouchers(orderBy, searchType,lastPage);
		loadJSON(json);
	}
	
	
	private void loadJSON(JSONObject json)
	{
		try
		{
		
		JSONArray vouchers = json.getJSONArray("items");
		totalPages = json.getInt("total_pages");
		totalItems = json.getInt("total_items");
		connectedUserID = json.getString("user_id");
		
		Log.e("total Pages", String.valueOf(totalPages));
		Log.e("total Items", String.valueOf(totalItems));

		for(int i = 0; i < vouchers.length(); i++){
			JSONObject voucherJSON = vouchers.getJSONObject(i);
			voucher theVoucher = new voucher(voucherJSON,connectedUserID);
			this.add(theVoucher);
		}
		
		
	   } catch (JSONException e) {
			e.printStackTrace();
	   }
	}
	
	
	public void deleteVoucher(String idvoucher)
	{
		
	}
	
	public void deleteVoucher(voucher theVoucher)
	{
		this.remove(theVoucher);
	}
	
	public int getTotalPages()
	{
		return this.totalPages;
	}
	
	public int getTotalItems()
	{
		return this.totalItems;
	}
		
	
}
