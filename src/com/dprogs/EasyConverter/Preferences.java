package com.dprogs.EasyConverter;

import com.dprogs.EasyConverter.R;
import com.flurry.android.FlurryAgent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class Preferences extends PreferenceActivity
{
    public final String TAG = "EasyConverter.Prefs";
	//decimal
    //public final static String Leadbolt_Section_ID = "404621415";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
  	  super.onCreate(savedInstanceState);
	  addPreferencesFromResource(R.xml.settings);
	}  
    
    @Override
    public void onStart()
    {
    	super.onStart();
    	FlurryAgent.setReportLocation(false);
    	FlurryAgent.onStartSession(this, var_const.API_Key_flurry);
      	FlurryAgent.logEvent("Preferences start");
      	FlurryAgent.setUserId(FlurryAgent.getPhoneId());
    }

    @Override
    public void onStop()
    {
    	super.onStop();

    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		//FLURRY STATS
        String str1, str2, str3;
        str1 = "Decimal places: "+prefs.getString("eConv_Decimal", "4");
        str2 = "Trim zeros: "+prefs.getBoolean("eConv_CutZero", true);
        str3 = "Use vibro: "+prefs.getBoolean("eConv_Vibro", false);
		Log.v(TAG, "before logevent");
    	FlurryAgent.logEvent(str1);
		Log.v(TAG, str1);
    	FlurryAgent.logEvent(str2);
		Log.v(TAG, str2);
    	FlurryAgent.logEvent(str3);
		Log.v(TAG, str3);
   	    FlurryAgent.onEndSession(this);
    }

    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    }

}
