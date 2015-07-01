package app.tmbao.comicreader.Library;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.tmbao.comicreader.R;

/**
 * Created by minhbao on 6/28/15.
 */
public class ComicTitleCursorAdapter extends CursorAdapter {

    ArrayList<ComicTitleItem> comicTitleItemList;
    TextView textView;
    ImageView imageView;

    public ComicTitleCursorAdapter(Context context, Cursor cursor, ArrayList<ComicTitleItem> comicTitleItemList) {
        super(context, cursor, false);
        this.comicTitleItemList = comicTitleItemList;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.comic_title_item_layout, parent, false);

        textView = (TextView) view.findViewById(R.id.text_comic_title);
        imageView = (ImageView) view.findViewById(R.id.image_comic_thumbnail);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        textView.setText(comicTitleItemList.get(cursor.getPosition()).getTitle());
        imageView.setImageBitmap(comicTitleItemList.get(cursor.getPosition()).getThumbnail());
    }
}
