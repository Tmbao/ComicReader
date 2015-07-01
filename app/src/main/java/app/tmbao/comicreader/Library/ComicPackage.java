package app.tmbao.comicreader.Library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by minhbao on 6/29/15.
 */
public class ComicPackage {
    private static ComicPackage ourInstance = new ComicPackage();

    private ComicPackage() {

    }

    public static ComicPackage getInstance() {
        return ourInstance;
    }

    private ComicTitleItem comicTitle;

    private ArrayList<String> pageTexts;
    private ArrayList<String> pagePaths;

    private ArrayList<ComicQuestion> questions;
    private ArrayList<String> questionPaths;

    private ComicQuestion loadQuestion(String path) {
        return ComicQuestion.load(path);
    }

    public ComicQuestion getQuestion(int index) {
        if (questions.get(index) == null)
            questions.set(index, loadQuestion(questionPaths.get(index)));
        return questions.get(index);
    }

    private String loadPageText(String path) {
        String ret = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            for (String line; (line = reader.readLine()) != null; )
                ret += line + "\n";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public String getPageText(int index) {
        if (pageTexts.get(index) == null)
            pageTexts.set(index, loadPageText(pagePaths.get(index)));
        return pageTexts.get(index);
    }

    public void setComic(ComicTitleItem comicTitle) {
        this.comicTitle = comicTitle;

//        Initialize pageFigures
        pagePaths = MediaHelper.getAllPageFiles(comicTitle.getPath());
        pageTexts = new ArrayList<>();
        for (int index = 0; index < getNumberOfPages(); index++)
            pageTexts.add(null);

//        Sample data
//        ComicQuestion.createFixtures(this.comicTitle.getPath());

//        Initialize questions
        questionPaths = MediaHelper.getAllQuestions(comicTitle.getPath());
        questions = new ArrayList<>();
        for (int index = 0; index < numberOfQuestions(); index++)
            questions.add(null);

    }

    public String getTitle() {
        return comicTitle.getTitle();
    }

    public int getNumberOfPages() {
        return pagePaths.size();
    }

    public int numberOfQuestions() {
        return questionPaths.size();
    }

}
