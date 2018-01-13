package twitterhashsearch.minkush.com.twitterhashsearch.apis.postapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import twitterhashsearch.minkush.com.twitterhashsearch.HashSearchTwiterActivity;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.APIUrlConstant;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.AlertDialog;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.AppController;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.CommonMethod;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.InternetConnection;

import static twitterhashsearch.minkush.com.twitterhashsearch.HashSearchTwiterActivity.GET_TWITTER_FRESH_DATA;


/**
 * Created by apple on 13/01/18.
 */

public class PostGetTweetApi {

    Context context;
    private static final String TAG = PostGetTweetApi.class.getSimpleName();


    /*
    Function to get all the tweets contaning passed hash tag
     */
    public void postGetTweetApi(String s_get_API, final HashSearchTwiterActivity hashSearchTwiterActivity
            ,int s_get_data_type) {

        context = hashSearchTwiterActivity;


        if (!InternetConnection.isInternetOn(hashSearchTwiterActivity)) {
            new AlertDialog().showAlerDiaog(context,"Alert!!","No Internet Connection");
            return;
        }
        final ProgressDialog pDialog = new ProgressDialog(context);
        if(s_get_data_type == GET_TWITTER_FRESH_DATA) {
            pDialog.setMessage("Load Tweets...");
            pDialog.show();
        }
        Log.i(TAG,"Get api for search hash tag tweet");

        StringRequest sr = new StringRequest(Request.Method.GET, s_get_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    ResponseGetTweetApi responseGetTweetApi = new ResponseGetTweetApi(new JSONObject(response));
                    hashSearchTwiterActivity.responseGetTweetApi(responseGetTweetApi);
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
                new AlertDialog().showAlerDiaog(context,"Alert!!",error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return CommonMethod.getHeaderTwitter(context);
            }
        };

        RequestQueue queue = AppController.getInstance().getRequestQueue();
        sr.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    public class ResponseGetTweetApi {

        public ArrayList<Tweet> tweetArrayList = new ArrayList<>();
        public ResponseGetTweetApi(JSONObject jsonObject) {
            try {

                JSONArray sessions = jsonObject.getJSONArray("statuses");
                for (int i = 0; i < sessions.length(); i++) {
                    tweetArrayList.add(new Tweet(sessions.getJSONObject(i)));
                }
                if (tweetArrayList.isEmpty()) {
                    Toast.makeText(context, "No Tweets", Toast.LENGTH_SHORT);
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public class Tweet{

        public String s_tweet_text,s_retweet_count,s_favorite_count,s_tweet_date;
        public User user;

        public Tweet(JSONObject session){
            try{

                 s_tweet_text = session.getString("text");
                 s_retweet_count = session.getString("retweet_count");
                 s_favorite_count = session.getString("favorite_count");

                String dte = session.getString("created_at");
                SimpleDateFormat dtformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzzz yyyy");
                Date d = dtformat.parse(dte);
                SimpleDateFormat dtfm = new SimpleDateFormat("EEE, MMM dd, hh:mm:ss a yyyy");
                 s_tweet_date = dtfm.format(d);

                user = new User(session.getJSONObject("user"));


            }catch (Exception e){
                e.printStackTrace();
            }

        }

        public class User{
            public String s_name,s_image_url;
            public User(JSONObject jsonObject){
                try{
                    s_name = jsonObject.getString("name");
                    s_image_url = jsonObject.getString("profile_image_url").replace("_normal","");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }

    }
}
