package app.tmbao.comicreader.Library;

import android.os.Bundle;

/**
 * Created by minhbao on 6/28/15.
 */
public class ComicPageIdItem {
    private int pageId;

    public ComicPageIdItem(int pageId) {
        this.pageId = pageId;
    }

    public ComicPageIdItem(Bundle bundle) {
        this.pageId = bundle.getInt("pageId");
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("pageId", pageId);
        return bundle;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

}
