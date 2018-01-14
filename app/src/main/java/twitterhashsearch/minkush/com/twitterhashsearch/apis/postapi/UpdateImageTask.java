package twitterhashsearch.minkush.com.twitterhashsearch.apis.postapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import twitterhashsearch.minkush.com.twitterhashsearch.customview.RoundNetworkImageview;

/**
 * Created by apple on 14/01/18.
 */

public class UpdateImageTask extends AsyncTask<String, Void, Bitmap> {

    RoundNetworkImageview bmImage;

    public UpdateImageTask(RoundNetworkImageview bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
