package twitterhashsearch.minkush.com.twitterhashsearch.apis.postapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Map;

import twitterhashsearch.minkush.com.twitterhashsearch.HashSearchTwiterActivity;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.APIUrlConstant;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.AlertDialog;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.AppController;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.CommonMethod;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.InternetConnection;
import static twitterhashsearch.minkush.com.twitterhashsearch.utility.Config.CONSUMER_KEY;
import static twitterhashsearch.minkush.com.twitterhashsearch.utility.Config.CONSUMER_SECRET;

/**
 * Created by apple on 13/01/18.
 */

public class PostGetTweeterAuthApi {

    Context context;
    String base64Encoded = "";
    private static final String TAG = PostGetTweeterAuthApi.class.getSimpleName();

    /*
    Function to get access token for twitter
     */
    public void postGetAuthTweetApi(final Map<String, String> keyvalueHashMap,
                                    final HashSearchTwiterActivity hashSearchTwiterActivity) {


        context = hashSearchTwiterActivity;

        if (!InternetConnection.isInternetOn(hashSearchTwiterActivity)) {
            new AlertDialog().showAlerDiaog(context, "Alert!!", "No Internet Connection");
            return;
        }
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Authenticate..Please Wait...");
        pDialog.show();
        try {
            String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
            String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");
            String combined = urlApiKey + ":" + urlApiSecret;
            base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG,"Get api for get access token");

        StringRequest sr = new StringRequest(Request.Method.POST, APIUrlConstant.GET_TOKEN_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    ResponseGetAuthTweetApi responseGetAuthTweetApi = new ResponseGetAuthTweetApi(new JSONObject(response));
                    hashSearchTwiterActivity.responseGetAuthTweetApi(responseGetAuthTweetApi);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                new AlertDialog().showAlerDiaog(context, "Alert!!", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                return keyvalueHashMap;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return CommonMethod.getHeaderAuthonitcate(base64Encoded);
            }
        };

        RequestQueue queue = AppController.getInstance().getRequestQueue();
        sr.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    public class ResponseGetAuthTweetApi {
        public String access_token, token_type;

        public ResponseGetAuthTweetApi(JSONObject jsonObject) {
            try {
                access_token = jsonObject.getString("access_token");
                token_type = jsonObject.getString("token_type");
            } catch (Exception e) {
                Log.e("jsonToAuthenticated", "Error retrieving JSON Authenticated Values : " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

}
