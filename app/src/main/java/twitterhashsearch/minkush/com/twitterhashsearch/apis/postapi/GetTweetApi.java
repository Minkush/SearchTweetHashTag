package twitterhashsearch.minkush.com.twitterhashsearch.apis.postapi;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import twitterhashsearch.minkush.com.twitterhashsearch.HashSearchTwiterActivity;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.APIUrlConstant;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.CommonMethod;

import static twitterhashsearch.minkush.com.twitterhashsearch.HashSearchTwiterActivity.GET_TWITTER_FRESH_DATA;
import static twitterhashsearch.minkush.com.twitterhashsearch.utility.Config.CONSUMER_KEY;
import static twitterhashsearch.minkush.com.twitterhashsearch.utility.Config.CONSUMER_SECRET;

/**
 * Created by apple on 14/01/18.
 */

public class GetTweetApi extends AsyncTask<Void, Void,String> {

    HashSearchTwiterActivity hashSearchTwiterActivity;
    ProgressDialog pDialog;
    private static final String TAG = GetTweetApi.class.getSimpleName();
    int s_get_data_type;
    String s_get_api;

    public GetTweetApi(final HashSearchTwiterActivity hashSearchTwiterActivit,String s_get_api,int s_get_data_type){
        this.hashSearchTwiterActivity = hashSearchTwiterActivit;
        this.s_get_api = s_get_api;
        pDialog = new ProgressDialog(hashSearchTwiterActivity);
        pDialog.setMessage("Loading..Getting Tweets...");
        pDialog.setCanceledOnTouchOutside(false);
        this.s_get_data_type = s_get_data_type;

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (pDialog != null) {
            if(s_get_data_type == GET_TWITTER_FRESH_DATA) {
                pDialog.setMessage("Load Tweets...");
                pDialog.show();
            }
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL(s_get_api);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            for (Map.Entry<String,String> headerparam : CommonMethod.getHeaderTwitter(hashSearchTwiterActivity).entrySet()) {
                urlConnection.setRequestProperty(headerparam.getKey(), headerparam.getValue());
            }

            int responseCode = urlConnection.getResponseCode();
            Log.i(TAG,url.getPath());
            Log.i(TAG,""+responseCode);

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }



    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
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

    public class ResponseGetTweetApi {

        public ArrayList<Tweet> tweetArrayList = new ArrayList<>();
        public ResponseGetTweetApi(JSONObject jsonObject) {
            try {

                JSONArray sessions = jsonObject.getJSONArray("statuses");
                for (int i = 0; i < sessions.length(); i++) {
                    tweetArrayList.add(new Tweet(sessions.getJSONObject(i)));
                }
                if (tweetArrayList.isEmpty()) {
                    Toast.makeText(hashSearchTwiterActivity, "No Tweets", Toast.LENGTH_SHORT);
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
