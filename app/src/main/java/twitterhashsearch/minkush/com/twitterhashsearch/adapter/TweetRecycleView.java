package twitterhashsearch.minkush.com.twitterhashsearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import twitterhashsearch.minkush.com.twitterhashsearch.R;
import twitterhashsearch.minkush.com.twitterhashsearch.apis.postapi.PostGetTweetApi;
import twitterhashsearch.minkush.com.twitterhashsearch.customview.RoundNetworkImageview;
import twitterhashsearch.minkush.com.twitterhashsearch.customview.ShrededNetworkImageView;
import twitterhashsearch.minkush.com.twitterhashsearch.utility.AppController;

/**
 * Created by apple on 13/01/18.
 */

public class TweetRecycleView extends RecyclerView.Adapter<TweetRecycleView.ViewHolder> {

    Context context;
    ArrayList<PostGetTweetApi.Tweet> tweetArrayList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class TweetHolder extends ViewHolder {

        TextView textView_text,textView_user_name,textView_date,textView_retweet,textView_favourite_count;
        RoundNetworkImageview imageView_user_pic;
        public TweetHolder(View v) {
            super(v);
            textView_text = (TextView) v.findViewById(R.id.adapter_twitter_textview);
            textView_date = (TextView) v.findViewById(R.id.adapter_twitter_date_textview);
            textView_retweet = (TextView) v.findViewById(R.id.adapter_twitter_retweet_count_textview);
            textView_favourite_count = (TextView) v.findViewById(R.id.adapter_twitter_favourite_count_textview);
            textView_user_name = (TextView) v.findViewById(R.id.adapter_twitter_name_textview);
            imageView_user_pic = (RoundNetworkImageview) v.findViewById(R.id.adapter_twitter_imageview_profilepic);



        }
    }


    public TweetRecycleView(Context context,ArrayList<PostGetTweetApi.Tweet> tweetArrayList) {
        this.context = context;
        this.tweetArrayList = tweetArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_twitter, viewGroup, false);
        return new TweetHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        TweetHolder tweetHolder = (TweetHolder) viewHolder;
        PostGetTweetApi.Tweet tweet = tweetArrayList.get(position);
        tweetHolder.textView_text.setText(Html.fromHtml("<b>" + "Tweet : " + "</b> " +tweet.s_tweet_text.toString()));
        tweetHolder.textView_date.setText(Html.fromHtml("<b>" + "Date : " + "</b> " +tweet.s_tweet_date.toString()));
        tweetHolder.textView_retweet.setText(Html.fromHtml("<b>" + "Retweet Count : " + "</b> " +tweet.s_retweet_count.toString()));
        tweetHolder.textView_favourite_count.setText(Html.fromHtml("<b>" + "Favourite Count : " + "</b> " +
                tweet.s_favorite_count.toString()));

        tweetHolder.textView_user_name.setText(tweet.user.s_name);

        AppController.getInstance().getVollayImageLoader().get(tweet.user.s_image_url,
                com.android.volley.toolbox.ImageLoader.
                        getImageListener(tweetHolder.imageView_user_pic,
                                R.drawable.placeholderuser, R.drawable.placeholderuser));
        tweetHolder.imageView_user_pic.setImageUrl(tweet.user.s_image_url,
                AppController.getInstance().getVollayImageLoader());

    }

    @Override
    public int getItemCount() {

        return tweetArrayList.size();

    }

    @Override
    public int getItemViewType(int position) {

        return 0;

    }


}
