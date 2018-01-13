package twitterhashsearch.minkush.com.twitterhashsearch.utility;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by apple on 13/01/18.
 */

public class AppController extends Application {

    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public com.android.volley.toolbox.ImageLoader getVollayImageLoader() {

        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getImageLoader();

        return mImageLoader;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
}
