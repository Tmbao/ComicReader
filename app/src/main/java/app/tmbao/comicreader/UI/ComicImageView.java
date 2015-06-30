package app.tmbao.comicreader.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by minhbao on 6/29/15.
 */
public class ComicImageView extends ImageView {

    public static interface OnSetImageListener {
        public void imageChanged();
    }

    public ComicImageView(Context context) {
        super(context);
    }

    public ComicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ComicImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private OnSetImageListener onSetImageListener;

    public void setOnSetImageListener(OnSetImageListener onSetImageListener) {
        this.onSetImageListener = onSetImageListener;
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        if (onSetImageListener != null)
            onSetImageListener.imageChanged();
    }
}
