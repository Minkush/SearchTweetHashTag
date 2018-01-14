package twitterhashsearch.minkush.com.twitterhashsearch.apis.postapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import twitterhashsearch.minkush.com.twitterhashsearch.HashSearchTwiterActivity;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.APIUrlConstant;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.CommonMethod;

import static twitterhashsearch.minkush.com.twitterhashsearch.utility.Config.CONSUMER_KEY;
import static twitterhashsearch.minkush.com.twitterhashsearch.utility.Config.CONSUMER_SECRET;

/**
 * Created by apple on 14/01/18.
 */

public class GetTweetAuthApi extends AsyncTask<String, Void,String> {

    HashSearchTwiterActivity hashSearchTwiterActivity;
    ProgressDialog pDialog;
    String base64Encoded;
    private static final String TAG = GetTweetAuthApi.class.getSimpleName();
    Map<String, String> paramsMap;

    public GetTweetAuthApi(final HashSearchTwiterActivity hashSearchTwiterActivity,Map<String, String> stringMap) {
        this.hashSearchTwiterActivity = hashSearchTwiterActivity;
        this.paramsMap = stringMap;
        pDialog = new ProgressDialog(hashSearchTwiterActivity);
        pDialog.setMessage("Authenticate..Please Wait...");
        pDialog.setCanceledOnTouchOutside(false);
        try {
            String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
            String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");
            String combined = urlApiKey + ":" + urlApiSecret;
            base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (pDialog != null) {
            pDialog.show();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(APIUrlConstant.GET_TOKEN_API);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            for (Map.Entry<String,String> headerparam : CommonMethod.getHeaderAuthonitcate(base64Encoded).entrySet()) {
                urlConnection.setRequestProperty(headerparam.getKey(), headerparam.getValue());
            }
            urlConnection.setDoOutput(true);
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,String> param : paramsMap.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(param.getKey());
                postData.append('=');
                postData.append(String.valueOf(param.getValue()));
            }
            byte[] postDataBytes = postData.toString().getBytes();
            urlConnection.getOutputStream().write(postDataBytes);

            int responseCode = urlConnection.getResponseCode();
            Log.i(TAG,url.getPath());
            Log.i(TAG,postData.toString());
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
            ResponseGetAuthTweetApi responseGetAuthTweetApi = new ResponseGetAuthTweetApi(new JSONObject(response));
            hashSearchTwiterActivity.responseGetAuthTweetApi(responseGetAuthTweetApi);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public class ResponseGetAuthTweetApi {
        public String access_token, token_type;

        public ResponseGetAuthTweetApi(JSONObject jsonObject) {
            try {
                access_token = jsonObject.getString("access_token");
                token_type = jsonObject.getString("token_type");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
