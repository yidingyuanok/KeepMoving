package com.mycheering.vpf.dailnetapn;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {
    static public final int PREF_TYPE_INT     = 0;
    static public final int PREF_TYPE_LONG    = 1;
    static public final int PREF_TYPE_FLOAT   = 2;
    static public final int PREF_TYPE_BOOLEAN = 3;
    static public final int PREF_TYPE_STRING  = 4;

    static public boolean set(Context context, String key, Object value) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        SharedPreferences.Editor prefEditor = pref.edit();

        if (value instanceof Integer) {
            prefEditor.putInt(key, (Integer) value);
        }
        else if (value instanceof Long) {
            prefEditor.putLong(key, (Long) value);
        }
        else if (value instanceof Boolean) {
            prefEditor.putBoolean(key, (Boolean) value);
        }
        else if (value instanceof String) {
            prefEditor.putString(key, (String) value);
        }
        else if (value instanceof Float) {
            prefEditor.putFloat(key, (Float) value);
        }

        return prefEditor.commit();
    }

    static public Object get(Context context, String key, int type, Object defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        Object value = null;

        switch (type) {
            case PrefUtils.PREF_TYPE_BOOLEAN: {
                value = pref.getBoolean(key, (Boolean) defaultValue);
            }
            break;

            case PrefUtils.PREF_TYPE_FLOAT: {
                value = pref.getFloat(key, (Float) defaultValue);
            }
            break;

            case PrefUtils.PREF_TYPE_INT: {
                value = pref.getInt(key, (Integer) defaultValue);
            }
            break;

            case PrefUtils.PREF_TYPE_LONG: {
                value = pref.getLong(key, (Long) defaultValue);
            }
            break;

            case PrefUtils.PREF_TYPE_STRING: {
                value = pref.getString(key, (String) defaultValue);
            }
            break;

            default:
                break;
        }

        return value;
    }

    static public boolean remove(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        return pref.edit().remove(key).commit();
    }

    /**
     * added by dinglin on 20130514
     * 仅仅在CheckReceiver中使用了
     *
     * @param context
     * @return
     */
    public static boolean clear(Context context) {
        SharedPreferences        pref       = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        return prefEditor.clear().commit();
    }
}
