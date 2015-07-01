package app.tmbao.comicreader.Library;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by minhbao on 6/29/15.
 */
public class ComicUserManager {
    private static ComicUserManager ourInstance = new ComicUserManager();

    public static ComicUserManager getInstance() {
        return ourInstance;
    }

    private ComicUserManager() {
    }

    public void setAchievementCallback(AchievementCallback achievementCallback) {
        this.achievementCallback = achievementCallback;
    }

    public interface AchievementCallback {
        void onNewAchievementUnlocked(ComicAchievement achievement);
    }

    private int score;
    private Set<String> answeredQuestions;
    SharedPreferences record;

    private AchievementCallback achievementCallback;

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

    public void answerQuestion(String alias) {
        answeredQuestions.add(alias);
    }

    public void updateQuestion(Context context, String alias) {
        if (answeredQuestions.contains(alias) == false) {
            answerQuestion(alias);
            score++;

            ComicAchievement achievement = ComicAchievementManager.getInstance().getAchievement(score);
            if (achievement.getRequiredScore() == score) {
                if (achievementCallback != null)
                    achievementCallback.onNewAchievementUnlocked(achievement);
            }
        }
    }

    public int getScore() {
        return score;
    }
}
