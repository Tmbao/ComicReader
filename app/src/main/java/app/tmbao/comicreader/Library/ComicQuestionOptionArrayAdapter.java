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
 * Created by minhbao on 6/29/15.
 */
public class ComicQuestionOptionArrayAdapter extends ArrayAdapter<String> {
    public ComicQuestionOptionArrayAdapter(Context context, ArrayList<String> options) {
        super(context, 0, options);
    }

    @Override
    public View getView(int index, View row, ViewGroup parent) {
        if (row == null)
            row = LayoutInflater.from(getContext()).inflate(R.layout.comic_question_option_layout, parent, false);

//        Binding Data
        TextView comicTitle = (TextView) row.findViewById(R.id.text_question_option);
        comicTitle.setText(getItem(index));
//        End of Binding Data

        return row;
    }
}
