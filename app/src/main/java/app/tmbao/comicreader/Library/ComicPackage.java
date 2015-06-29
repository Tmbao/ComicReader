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
        Bitmap bitmap = BitmapFactory.decodeFile(path);

//            Scale bitmap
        double ratio = (double) bitmap.getWidth() / bitmap.getHeight();
        int newWidth = Math.min(R.integer.max_page_width, bitmap.getWidth());
        int newHeight = Math.min(R.integer.max_page_height, bitmap.getHeight());

        newWidth = (int) Math.min(newWidth, newHeight * ratio);
        newHeight = (int) Math.min(newHeight, newWidth / ratio);

        bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);

        return bitmap;
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
        ComicQuestion.createFixtures(this.comicTitle.getPath());

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
