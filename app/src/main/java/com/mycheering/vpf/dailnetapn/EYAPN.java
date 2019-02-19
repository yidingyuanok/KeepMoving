package com.mycheering.vpf.dailnetapn;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.text.TextUtils;

import com.mycheering.vpf.BuildConfig;
import com.mycheering.vpf.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

// import org.modu.dail.net.L;

public class EYAPN {

	private static final String TAG = "EYU-APN";
	private static final String[] APN_PROJECTION = { 
		Telephony.Carriers.TYPE, // 0
		Telephony.Carriers.MMSC, // 1
		Telephony.Carriers.MMSPROXY, // 2
		Telephony.Carriers.MMSPORT, // 3
		Telephony.Carriers._ID, // 4
		Telephony.Carriers.CURRENT, // 5
		Telephony.Carriers.NUMERIC, // 6
		Telephony.Carriers.NAME, // 7
		Telephony.Carriers.MCC, // 8
		Telephony.Carriers.MNC, // 9
		Telephony.Carriers.APN, // 10
		"sub_id"	//Telephony.Carriers.SUBSCRIPTION_ID // 11
	};
	private static final int COLUMN_TYPE = 0;
	private static final int COLUMN_MMSC = 1;
	private static final int COLUMN_MMSPROXY = 2;
	private static final int COLUMN_MMSPORT = 3;

	public static final String RESTORE_CARRIERS_URI = "content://telephony/carriers/restore";
	public static final String PREFERRED_APN_URI = "content://telephony/carriers/preferapn";

	/**
	 * 
	 * @param context
	 * @param apnName 类似cmwap这种形式
	 * @param subId 默认�?0
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static APN load(Context context, String apnName, int subId) {

		if(BuildConfig.DEBUG) L.i("Loading APN using name " + apnName);

		String selection = null;
		String[] selectionArgs = null;
		apnName = apnName != null ? apnName.trim() : null;

		if (!TextUtils.isEmpty(apnName)) {
			// selection += " AND " + Telephony.Carriers.APN + "=?";
			selection = Telephony.Carriers.APN + "=?";
			selectionArgs = new String[] { apnName };

		}

		Cursor cursor = null;

		try {
			cursor = SqliteWrapper.query(context, context.getContentResolver(), Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "/subId/" + subId), APN_PROJECTION, selection, selectionArgs, null/* sortOrder */);
			if (cursor != null) {
				String mmscUrl = null;
				String proxyAddress = null;
				int proxyPort = -1;

				while (cursor.moveToNext()) {
					if (IsValidApnType(cursor.getString(COLUMN_TYPE), PhoneConstants.APN_TYPE_MMS)) {
						mmscUrl = EYAPN.trimWithNullCheck(cursor.getString(COLUMN_MMSC));
						if (TextUtils.isEmpty(mmscUrl)) {
							continue;
						}

						mmscUrl = NetworkUtils.trimV4AddrZeros(mmscUrl);
						try {
							new URI(mmscUrl);
						} catch (URISyntaxException e) {
							return null;
						}

						proxyAddress = EYAPN.trimWithNullCheck(cursor.getString(COLUMN_MMSPROXY));
						if (!TextUtils.isEmpty(proxyAddress)) {
							proxyAddress = NetworkUtils.trimV4AddrZeros(proxyAddress);
							final String portString = EYAPN.trimWithNullCheck(cursor.getString(COLUMN_MMSPORT));
							if (portString != null) {
								try {
									proxyPort = Integer.parseInt(portString);
								} catch (NumberFormatException e) {
//									if (EYLog.EXCEPTION)
									if(BuildConfig.DEBUG) L.i("Invalid port " + portString);

									return null;
								}
							}
						}
						return new APN(mmscUrl, proxyAddress, proxyPort);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return null;
	}

	public static String trimWithNullCheck(final String _szValue) {
		return _szValue != null ? _szValue.trim() : null;
	}

	public static boolean IsValidApnType(String _szTypes, String _szRequestType) {
		// If APN type is unspecified, assume APN_TYPE_ALL.
		for (String szItem : _szTypes.split(",")) {
			if (szItem.equals(_szRequestType) || szItem.equals(PhoneConstants.APN_TYPE_ALL)) {
				return true;

			} /* End if () */

		} /* End for () */

		return false;
	}

	public static class APN {
		public String mServiceCenter = null;
		public String mProxyAddress = null;
		public int mProxyPort = -1;

		public APN(String _szMmscUrl, String _szProxyAddr, int _nProxyPort) {
			mServiceCenter = _szMmscUrl;
			mProxyAddress = _szProxyAddr;
			mProxyPort = _nProxyPort;
		}

		
		
		@Override
		public String toString() {
			return "APN [mServiceCenter=" + mServiceCenter + ", mProxyAddress=" + mProxyAddress + ", mProxyPort=" + mProxyPort + "]";
		}
		
		public JSONObject toJSON(){
			JSONObject object = new JSONObject();
			
			try {
				if(mServiceCenter!=null){
					object.put("center", mServiceCenter);
				}
			} catch (JSONException e) {

			}

			try {
				if(mProxyAddress != null){
					object.put("address", mProxyAddress);
				}
			} catch (JSONException e) {
				
			}
			
			try {
				if(mProxyPort != -1){
					object.put("port", mProxyPort);
				}
			} catch (JSONException e) {
				
			}
			
			return object;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((mProxyAddress == null) ? 0 : mProxyAddress.hashCode());
			result = prime * result + mProxyPort;
			result = prime * result + ((mServiceCenter == null) ? 0 : mServiceCenter.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			APN other = (APN) obj;
			if (mProxyAddress == null) {
				if (other.mProxyAddress != null)
					return false;
			} else if (!mProxyAddress.equals(other.mProxyAddress))
				return false;
			if (mProxyPort != other.mProxyPort)
				return false;
			if (mServiceCenter == null) {
				if (other.mServiceCenter != null)
					return false;
			} else if (!mServiceCenter.equals(other.mServiceCenter))
				return false;
			return true;
		}

	}
}
