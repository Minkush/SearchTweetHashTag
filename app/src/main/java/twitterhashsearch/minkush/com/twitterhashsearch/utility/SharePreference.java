package twitterhashsearch.minkush.com.twitterhashsearch.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by apple on 13/01/18.
 */

public class SharePreference {

    public static void saveKeyValueSharePreference(Context context,String s_key,String s_value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(s_key,s_value);
        editor.apply();
    }

    public static String getKeyValueSharePreference(Context context,String s_key){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String s_value = preferences.getString(s_key, null);

        return s_value;
    }

    public static class SharePrefrenceKeyConstant {

        public  static String s_key_twitter_access_token = "access_token";

    }

}
