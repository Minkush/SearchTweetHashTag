package twitterhashsearch.minkush.com.twitterhashsearch.utility;

import android.app.Application;


/**
 * Created by apple on 13/01/18.
 */

public class AppController extends Application {

    private static AppController mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }


}
