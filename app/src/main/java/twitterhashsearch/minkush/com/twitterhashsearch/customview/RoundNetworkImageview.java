package twitterhashsearch.minkush.com.twitterhashsearch.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.github.siyamed.shapeimageview.shader.CircleShader;
import com.github.siyamed.shapeimageview.shader.ShaderHelper;

/**
 * Created by apple on 14/01/18.
 */

public class RoundNetworkImageview extends ShrededNetworkImageView {

    private CircleShader shader;

    public RoundNetworkImageview(Context context) {
        super(context);
    }

    public RoundNetworkImageview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundNetworkImageview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        shader = new CircleShader();
        return shader;
    }

}
