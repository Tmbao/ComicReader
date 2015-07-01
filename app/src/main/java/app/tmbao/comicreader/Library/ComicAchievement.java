package app.tmbao.comicreader.Library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by minhbao on 6/30/15.
 */
public class ComicAchievement implements Comparator<ComicAchievement>, Comparable<ComicAchievement> {
    private int requiredScore;
    private String levelImagePath;
    private String levelName;

    public ComicAchievement(int requiredScore, String levelName) {
        this.requiredScore = requiredScore;
        this.levelName = levelName;
    }

    public ComicAchievement(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            requiredScore = jsonObject.getInt("RequiredScore");
            levelName = jsonObject.getString("LevelName");
            levelImagePath = jsonObject.getString("LevelImage");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String toJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("RequiredScore", requiredScore);
            jsonObject.put("LevelName", levelName);
            jsonObject.put("LevelImage", levelImagePath);

            return jsonObject.toString(4);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getRequiredScore() {
        return requiredScore;
    }

    public String getLevelName() {
        return levelName;
    }

    public Bitmap getLevelImage(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier(levelImagePath, "drawable", context.getPackageName()));
    }

    @Override
    public int compare(ComicAchievement lhs, ComicAchievement rhs) {
        return lhs.getRequiredScore() - rhs.getRequiredScore();
    }

    @Override
    public int compareTo(ComicAchievement another) {
        return getRequiredScore() - another.getRequiredScore();
    }
}
