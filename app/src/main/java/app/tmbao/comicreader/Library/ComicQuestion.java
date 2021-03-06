package app.tmbao.comicreader.Library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import app.tmbao.comicreader.R;

/**
 * Created by minhbao on 6/29/15.
 */
public class ComicQuestion {
    private String directory;
    private String alias;

    private String questionStatement;
    private ArrayList<String> options;
    private int correctOption;

    public String getQuestionStatement() {
        return questionStatement;
    }

    public void setQuestionStatement(String questionStatement) {
        this.questionStatement = questionStatement;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public ComicQuestion() {
        options = new ArrayList<>();
    }

    public ComicQuestion(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            questionStatement = jsonObject.getString("Statement");

            JSONArray jsonArray = jsonObject.getJSONArray("Options");
            options = new ArrayList<>();
            for (int index = 0; index < jsonArray.length(); index++)
                options.add(jsonArray.get(index).toString());

            correctOption = jsonObject.getInt("Correct");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Statement", questionStatement);

            JSONArray jsonArray = new JSONArray();
            for (int index = 0; index < options.size(); index++)
                jsonArray.put(options.get(index));
            jsonObject.put("Options", jsonArray);
            jsonObject.put("Correct", correctOption);

            return jsonObject.toString(4);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(String path) {
        try {
            File file = new File(path);
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.write(toJSON());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ComicQuestion load(String path) {
        try {
            File file = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String jsonString = "";
            for (String line; (line = reader.readLine()) != null; )
                jsonString += line;
            reader.close();

            ComicQuestion comicQuestion = new ComicQuestion(jsonString);
            comicQuestion.setAlias(MediaHelper.getFileName(path));
            comicQuestion.setDirectory(MediaHelper.getDirectory(path));

            return comicQuestion;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
