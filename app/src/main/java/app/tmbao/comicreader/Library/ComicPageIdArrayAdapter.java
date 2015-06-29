package app.tmbao.comicreader.Library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.tmbao.comicreader.R;

/**
 * Created by minhbao on 6/28/15.
 */
public class ComicPageIdArrayAdapter extends ArrayAdapter<ComicPageIdItem> {
    public ComicPageIdArrayAdapter(Context context, ArrayList<ComicPageIdItem> pageIdItems) {
        super(context, 0, pageIdItems);
    }

    @Override
    public View getView(int index, View row, ViewGroup parent) {
        if (row == null)
            row = LayoutInflater.from(getContext()).inflate(R.layout.comic_page_id_item_layout, parent, false);

//        Binding Data
        TextView comicTitle = (TextView) row.findViewById(R.id.comic_page_id);
        comicTitle.setText("Page #" + String.valueOf(getItem(index).getPageId()));
//        End of Binding Data

        return row;
    }
}
