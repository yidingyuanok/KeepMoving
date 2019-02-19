package com.mycheering.vpf.dailnetapn;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.mycheering.vpf.BuildConfig;
import com.mycheering.vpf.utils.L;

import java.lang.reflect.Method;

//import android.telephony.SubscriptionManager;

public class SimIndexUtils {
	public static final String TAG = "AdUtils";



	public static int getDefaultDataSubId(Context _Context) {
		boolean		bDone	= false;
		int 		nSubId 	= 0;
		
		try {
			Class<?>	stClass		= Class.forName("android.telephony.SubscriptionManager");
			final Method stMethod 	= stClass.getMethod("getDefaultDataSubId");

			if (stMethod != null) {
				nSubId = (Integer) stMethod.invoke(stClass);
			}
			bDone	= true;
		} catch (final Exception _E) {
			if(BuildConfig.DEBUG) L.i("EYSubscriptionManager::getDefaultSubId--> Exception : " + _E);
		}
		
		if (false == bDone) {
			nSubId	= 0;
			
			try {

				TelephonyManager  stTM  	= (TelephonyManager) _Context.getSystemService(Context.TELEPHONY_SERVICE);
				final Method 	stMethod= stTM.getClass().getMethod("getSmsDefaultSim");
				if (stMethod != null) {
					nSubId = (Integer) stMethod.invoke(stTM);
				}

			} catch (final Exception _E) {
				if(BuildConfig.DEBUG) L.i("EYSubscriptionManager::getDefaultSubId--> Exception : " + _E);
			}
		}
//
//      if (IdeaConfig.IDEA_DEBUG) Log.d(IdeaConfig.IDEA_TAG, "MTKProvider::GetDefaultSim--> Entry :");
//
//      mDefaultSIM = ITM.getSmsDefaultSim();
		
		return nSubId;
	}
	


}
