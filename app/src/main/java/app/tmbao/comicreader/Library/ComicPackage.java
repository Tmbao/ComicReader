package app.tmbao.comicreader.Library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

import app.tmbao.comicreader.R;

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

    private ArrayList<Bitmap> pages;
    private ArrayList<String> pagePaths;

    private ArrayList<ComicQuestion> questions;
    private ArrayList<String> questionPaths;

    private Bitmap loadPage(String path) {
        return MediaHelper.loadPage(path, R.dimen.max_page_width, R.dimen.max_figure_height);
    }

    private ComicQuestion loadQuestion(String path) {
        return ComicQuestion.load(path);
    }

    public Bitmap getPage(int index) {
        if (pages.get(index) == null)
            pages.set(index, loadPage(pagePaths.get(index)));
        return pages.get(index);
    }

    public ComicQuestion getQuestion(int index) {
        if (questions.get(index) == null)
            questions.set(index, loadQuestion(questionPaths.get(index)));
        return questions.get(index);
    }

    public void setComic(ComicTitleItem comicTitle) {
        this.comicTitle = comicTitle;

//        Initialize pages
        pagePaths = MediaHelper.getAllPageFiles(comicTitle.getPath());
        pages = new ArrayList<>();
        for (int index = 0; index < numberOfPages(); index++)
            pages.add(null);

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

    public int numberOfPages() {
        return pagePaths.size();
    }

    public int numberOfQuestions() {
        return questionPaths.size();
    }

}
