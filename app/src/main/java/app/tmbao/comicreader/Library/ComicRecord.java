package app.tmbao.comicreader.Library;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

import app.tmbao.comicreader.R;

/**
 * Created by minhbao on 6/29/15.
 */
public class ComicRecord {
    private static ComicRecord ourInstance = new ComicRecord();

    public static ComicRecord getInstance() {
        return ourInstance;
    }

    private ComicRecord() {
    }

    private int score;
    private Set<String> answeredQuestions;
    SharedPreferences record;

    public void loadRecord(Context context) {
        record = PreferenceManager.getDefaultSharedPreferences(context);
        score = record.getInt("score", 0);
        answeredQuestions = record.getStringSet("answeredQuestion", new HashSet<String>());
    }

    public void saveRecord() {
        SharedPreferences.Editor editor = record.edit();
        editor.putInt("score", score);
        editor.putStringSet("answeredQuestions", answeredQuestions);
        editor.commit();
    }

    public void updateQuestion(String alias) {
        if (answeredQuestions.contains(alias) == false) {
            answeredQuestions.add(alias);
            score++;
        }
    }

    public int getScore() {
        return score;
    }
}
