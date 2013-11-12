package com.beevou.android.scanner;


import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import libraries.SimpleCrypto;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

public class Beevou_Scanner extends Application {
	
	
	private String accessToken;
	private String refreshToken;
	private static Beevou_Scanner m_Instance;
	private String userName;
	private String userPassword;
	private String loginMode = "AUTO";
	static String seed = "MY SEED Password - 5555";
	private String advancedSecurity = "n";

	
	@Override
    public void onCreate() {
        super.onCreate();
	}
	

	public static 	SharedPreferences prefs	= null;
	public Beevou_Scanner() {
		super();
		m_Instance = this;
	}

	public static Beevou_Scanner getInstance() {

		if(m_Instance == null) {
			synchronized(Beevou_Scanner.class) {
				if(m_Instance == null) new Beevou_Scanner();
			}
		}
		
		
		if (prefs == null) prefs = m_Instance.getSharedPreferences("BVOU1", 0);
		return m_Instance;
	}
	
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String value) {
        this.accessToken = value;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String value) {
        this.refreshToken = value;
    }
	public String getUserName() {
		try {
			String uname = prefs.getString("0002", null);
			if (uname != null &&! uname.equals(""))
			{
				//userName = SimpleCrypto.decrypt(seed,uname);
				userName = uname;  
			} else {
				userName = "";	
			}
		} catch (Exception e) {
			Log.e("getUserName","Exception decrypting username from prefs "+e.getMessage());
			userName = ""; 
		}
		return userName;
	}
	public void setUserName(String userName) {
		try {
			prefs.edit().putString("0001",SimpleCrypto.encrypt(seed,"BEEVOU ANDROID APPLICATION")).commit();
			//prefs.edit().putString("0002",SimpleCrypto.encrypt(seed,userName)).commit();
			prefs.edit().putString("0002",userName).commit();
		} catch (Exception e) {
			Log.e("getUserName","Exception encrypting username from prefs");
		}
		this.userName = userName;
	}
	public String getUserPassword() {
		
		try {
			String upass = prefs.getString("0003", null);
			if (upass != null &&! upass.equals(""))
			{
				//userPassword = SimpleCrypto.decrypt(seed,upass);
				userPassword = upass;
			} else {
				userPassword = "";	
			}
		} catch (Exception e) {
			Log.e("getUserName","Exception decrypting userpass from prefs");
			userPassword = "";
		}
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		try {
			//prefs.edit().putString("0003",SimpleCrypto.encrypt(seed,userPassword)).commit();
			prefs.edit().putString("0003",userPassword).commit();  
		} catch (Exception e) {
			Log.e("getUserName","Exception encrypting userpass from prefs");
		}
		this.userPassword = userPassword;
	}
	public String getLoginMode() {
		String lMode = prefs.getString("LOGIN_MODE", null);
		if (lMode != null)
			loginMode = lMode;
		return loginMode;
	}
	public void setLoginMode(String loginMode) {
		prefs.edit().putString("LOGIN_MODE",loginMode).commit();
		this.loginMode = loginMode;
	}
	public String getAdvancedSecurity() {
		return advancedSecurity;
	}
	public void setAdvancedSecurity(String advancedSecurity) {
		this.advancedSecurity = advancedSecurity;
	}
    
    
    


}
