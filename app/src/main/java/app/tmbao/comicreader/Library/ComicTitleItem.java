package app.tmbao.comicreader.Library;

import android.os.Bundle;

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
}
