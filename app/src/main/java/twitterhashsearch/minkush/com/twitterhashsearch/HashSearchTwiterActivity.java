package twitterhashsearch.minkush.com.twitterhashsearch;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import twitterhashsearch.minkush.com.twitterhashsearch.apis.postapi.PostGetTweetApi;
import twitterhashsearch.minkush.com.twitterhashsearch.apis.postapi.PostGetTweeterAuthApi;
import twitterhashsearch.minkush.com.twitterhashsearch.adapter.TweetRecycleView;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.APIKeyConstant;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.APIUrlConstant;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.SharePreference;

public class HashSearchTwiterActivity extends AppCompatActivity {

    EditText editText;
    RecyclerView recyclerViewTweets;
    TweetRecycleView tweetRecycleView;
    private LinearLayoutManager mLayoutManager;
    Button button_search;
    final Handler tweetIntervalhandler = new Handler();
    public static long API_HIT_INTERVAL_TIME = 2000;
    public ArrayList<PostGetTweetApi.Tweet> tweetArrayList = new ArrayList<>();
    public static int GET_TWITTER_FRESH_DATA = 1;
    public static int GET_TWITTER_UPDATE_DATA = 2;

    private static final String TAG = HashSearchTwiterActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hash_search_twiter);
        editText = (EditText)findViewById(R.id.activity_hash_search_twitter_edittext);
        button_search = (Button)findViewById(R.id.activity_hash_search_twitter_search_button);
        recyclerViewTweets = (RecyclerView)findViewById(R.id.activity_hash_search_twitter_recycleview);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTweets.setLayoutManager(mLayoutManager);

        recyclerViewTweets.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));


        tweetRecycleView = new TweetRecycleView(this,tweetArrayList);
        recyclerViewTweets.setAdapter(tweetRecycleView);

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_hash_tag = editText.getText().toString();
                if(s_hash_tag.equals("")){
                    editText.setError("Please enter hash tag for search");
                    return;
                }
                editText.setError(null);
                Map map = new HashMap();
                map.put(APIKeyConstant.AUTHENTICATION_GRANT_TYPE,"client_credentials");
                new PostGetTweeterAuthApi().postGetAuthTweetApi(map,HashSearchTwiterActivity.this);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String s_hash_tag = editText.getText().toString();
        if(!s_hash_tag.equals("") &&
                SharePreference.getKeyValueSharePreference(this, SharePreference.
                        SharePrefrenceKeyConstant.s_key_twitter_access_token) != null) {
            tweetIntervalhandler.postDelayed(tweetIntervalRunnable, API_HIT_INTERVAL_TIME);
        }
    }

    public void responseGetAuthTweetApi(PostGetTweeterAuthApi.ResponseGetAuthTweetApi responseGetAuthTweetApi){

        SharePreference.saveKeyValueSharePreference(this,
                SharePreference.SharePrefrenceKeyConstant.s_key_twitter_access_token,
                responseGetAuthTweetApi.access_token);
        getTweetApi(GET_TWITTER_FRESH_DATA);

    }

    public void getTweetApi(int freshUpdateType){
        String s_hash_tag = editText.getText().toString();
        String s_get_Api = APIUrlConstant.GET_SEARCH_HASHTAG_API + "q=" + s_hash_tag;
        new PostGetTweetApi().postGetTweetApi(s_get_Api,this,freshUpdateType);
    }

    public void responseGetTweetApi( PostGetTweetApi.ResponseGetTweetApi responseGetTweetApi){

        tweetArrayList.clear();
        tweetArrayList.addAll(responseGetTweetApi.tweetArrayList);
        tweetRecycleView.notifyDataSetChanged();
        tweetIntervalhandler.postDelayed(tweetIntervalRunnable, API_HIT_INTERVAL_TIME );

    }

    Runnable tweetIntervalRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG,"Get Hash Tag search tweet handler START");
            getTweetApi(GET_TWITTER_UPDATE_DATA);
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        if(tweetIntervalhandler!=null){
            Log.i(TAG,"Get hash tag tweet handler STOP");
            tweetIntervalhandler.removeCallbacks(tweetIntervalRunnable);
        }
    }

}
