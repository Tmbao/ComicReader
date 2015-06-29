package app.tmbao.comicreader.Library;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.tmbao.comicreader.R;

/**
 * Created by minhbao on 6/28/15.
 */
public class ComicTitleArrayAdapter extends ArrayAdapter<ComicTitleItem> {

    public ComicTitleArrayAdapter(Context context, ArrayList<ComicTitleItem> comicTitleItemList) {
        super(context, 0, comicTitleItemList);
    }

    @Override
    public View getView(int index, View row, ViewGroup parent) {
        if (row == null)
            row = LayoutInflater.from(getContext()).inflate(R.layout.comic_title_item_layout, parent, false);

//        Binding Data
        TextView comicTitle = (TextView) row.findViewById(R.id.text_comic_title);
        comicTitle.setText(getItem(index).getTitle());
        ImageView comicThumbnail = (ImageView) row.findViewById(R.id.image_comic_thumbnail);
        comicThumbnail.setImageBitmap(getItem(index).getThumbnail());
//        End of Binding Data

        return row;
    }
}
