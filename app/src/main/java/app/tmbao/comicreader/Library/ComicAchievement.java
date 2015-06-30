package app.tmbao.comicreader.Library;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by minhbao on 6/30/15.
 */
public class ComicAchievement implements Comparator<ComicAchievement>, Comparable<ComicAchievement> {
    private int requiredScore;
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String toJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("RequiredScore", requiredScore);
            jsonObject.put("LevelName", levelName);

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

    @Override
    public int compare(ComicAchievement lhs, ComicAchievement rhs) {
        return lhs.getRequiredScore() - rhs.getRequiredScore();
    }

    @Override
    public int compareTo(ComicAchievement another) {
        return getRequiredScore() - another.getRequiredScore();
    }
}
