package app.tmbao.comicreader.Library;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by minhbao on 7/3/15.
 */
public class ComicTitleSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = ComicTitleSuggestionsProvider.class.getName();

    public static final int MODE = DATABASE_MODE_QUERIES;

    public ComicTitleSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
