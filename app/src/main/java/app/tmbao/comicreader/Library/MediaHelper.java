package app.tmbao.comicreader.Library;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by minhbao on 6/29/15.
 */
public class MediaHelper {

    public static String getApplicationMediaDirectory() {
        File mediaStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Comic Reader");

        if (!mediaStorageDirectory.exists())
            if (!mediaStorageDirectory.mkdirs())
                return null;

        try {
            return mediaStorageDirectory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<ComicTitleItem> getAllComicTitle() {
        ArrayList<ComicTitleItem> comicDirectories = new ArrayList<>();

        String applicationMediaDirectory = getApplicationMediaDirectory();
        if (applicationMediaDirectory != null) {
            File directory = new File(applicationMediaDirectory);
            if (directory.exists()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files)
                        if (file != null)
                            if (file.isDirectory())
                                try {
                                    comicDirectories.add(new ComicTitleItem(file.getName(), file.getCanonicalPath()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                }
            }
        }

        return comicDirectories;
    }

    public static ArrayList<String> getAllPageFiles(String comicDirectory) {
        ArrayList<String> allPageFiles = new ArrayList<>();

        if (comicDirectory != null) {
            File directory = new File(comicDirectory);
            if (directory.exists()) {
                File[] files = directory.listFiles();

                if (files != null) {
                    for (File file : files)
                        if (file != null)
                            if (!file.isDirectory())
                                try {
                                    allPageFiles.add(file.getCanonicalPath());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                }
            }
        }

        Collections.sort(allPageFiles);
        return allPageFiles;
    }

    public static ArrayList<String> getAllQuestions(String comicDirectory) {
        ArrayList<String> allQuestionFiles = new ArrayList<>();

        if (comicDirectory != null) {
            File directory = new File(comicDirectory, "Questions");

            if (!directory.exists())
                if (!directory.mkdirs())
                    return null;

            if (directory.exists()) {
                File[] files = directory.listFiles();

                if (files != null) {
                    for (File file : files)
                        if (file != null)
                            if (!file.isDirectory())
                                try {
                                    allQuestionFiles.add(file.getCanonicalPath());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                }
            }
        }

        return allQuestionFiles;
    }

    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
