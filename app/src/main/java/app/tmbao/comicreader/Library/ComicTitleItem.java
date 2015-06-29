package app.tmbao.comicreader.Library;

import android.graphics.Bitmap;
import android.os.Bundle;

import app.tmbao.comicreader.R;

/**
 * Created by minhbao on 6/28/15.
 */
public class ComicTitleItem {
    private String title;
    private String path;

    public ComicTitleItem(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public ComicTitleItem(Bundle bundle) {
        this.title = bundle.getString("title");
        this.path = bundle.getString("path");
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("path", path);
        return bundle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getThumbnail() {
        return MediaHelper.loadPage(path + "/_.jpg", R.dimen.max_thumbnail_width, R.dimen.max_thumbnail_height);
    }
}
