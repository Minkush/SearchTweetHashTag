package twitterhashsearch.minkush.com.twitterhashsearch.utility;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by apple on 13/01/18.
 */

public class AlertDialog {

    public static void showAlerDiaog(Context context, String s_title, String s_message){


        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(context);
        builder1.setTitle(s_title);
        builder1.setMessage(s_message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert11 = builder1.create();
        alert11.show();


    }
}
