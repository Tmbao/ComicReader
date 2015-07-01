package app.tmbao.comicreader.Library;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import app.tmbao.comicreader.R;

/**
 * Created by minhbao on 6/30/15.
 */
public class ComicAchievementManager {
    private static ComicAchievementManager ourInstance = new ComicAchievementManager();

    public static ComicAchievementManager getInstance() {
        return ourInstance;
    }

    ArrayList<ComicAchievement> achievements;

    private ComicAchievementManager() {
    }

    public ComicAchievement getAchievement(int score) {
        int left = 0, right = achievements.size() - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (achievements.get(mid).getRequiredScore() <= score)
                left = mid + 1;
            else
                right = mid;
        }
        if (achievements.get(left).getRequiredScore() > score)
            left--;
        return achievements.get(left);
    }

    public ComicAchievement getNextAchievement(int score) {
        int left = 0, right = achievements.size() - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (achievements.get(mid).getRequiredScore() <= score)
                left = mid + 1;
            else
                right = mid;
        }
        return achievements.get(left);
    }

    public void load(Context context) {
        achievements = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.levels)));
            String jsonString = "";
            for (String line; (line = reader.readLine()) != null; )
                jsonString += line;
            reader.close();

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int index = 0; index < jsonArray.length(); index++)
                achievements.add(new ComicAchievement(jsonArray.getString(index)));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(achievements);
    }
}
