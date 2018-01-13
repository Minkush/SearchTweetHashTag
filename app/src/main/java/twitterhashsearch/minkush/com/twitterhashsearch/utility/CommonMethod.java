package twitterhashsearch.minkush.com.twitterhashsearch.utility;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 13/01/18.
 */

public class CommonMethod {

    public static Map<String, String> getHeaderAuthonitcate(String base64Encoded) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            params.put("Authorization", "Basic " + base64Encoded);
        return params;

    }

    public static Map<String, String> getHeaderTwitter(Context context) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("Content-Type", "application/json");
        params.put("Authorization", "Bearer " + SharePreference.getKeyValueSharePreference(context, SharePreference.SharePrefrenceKeyConstant.s_key_twitter_access_token));
        return params;

    }

}
