package app.tmbao.comicreader.Library;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import app.tmbao.comicreader.Activities.ComicQuestionActivity;
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

    public void load(Context context) {
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

    public void updateQuestion(Context context, String alias) {
        if (answeredQuestions.contains(alias) == false) {
            answeredQuestions.add(alias);
            score++;

            ComicAchievement achievement = ComicAchievementManager.getInstance().getAchievement(score);
            if (achievement.getRequiredScore() == score) {
//                Show congratulation message
                Toast.makeText(context, "Congratulation, you now become " + achievement.getLevelName(), Toast.LENGTH_SHORT).show();
//                Play congratulation song
            }
        }
    }

    public int getScore() {
        return score;
    }

    public void showQuestionHint(Context context, int questionId) {
        ComicPackage comicPackage = ComicPackage.getInstance();
        answeredQuestions.add(comicPackage.getTitle() + "_" + comicPackage.getQuestion(questionId).getAlias());

        Toast.makeText(context, "The answer is " + comicPackage.getQuestion(questionId).getOptions().get(
                comicPackage.getQuestion(questionId).getCorrectOption()), Toast.LENGTH_SHORT).show();
    }
}
