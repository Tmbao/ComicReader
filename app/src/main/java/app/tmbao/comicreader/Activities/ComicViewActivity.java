package app.tmbao.comicreader.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;

import java.util.Locale;

import app.tmbao.comicreader.Library.ComicPackage;
import app.tmbao.comicreader.Library.ComicPageIdItem;
import app.tmbao.comicreader.R;

public class ComicViewActivity extends Activity {

    TextToSpeech textToSpeech;

    ComicPackage comicPackage;
    ComicPageIdItem comicPageId;

    ProgressBar progressPage;

    WebView webView;

    private void fetchingPages() {
        comicPackage = ComicPackage.getInstance();

        Bundle bundle = getIntent().getBundleExtra("ComicPageId");
//        Set currentPageView
        if (bundle != null)
            comicPageId = new ComicPageIdItem(bundle);
        else
            comicPageId = new ComicPageIdItem(0);

//        Jump to pageId
        updatePage();
    }

    private void initializeComponents() {
        comicPackage = ComicPackage.getInstance();
        getActionBar().setTitle(comicPackage.getTitle());
        webView = (WebView) findViewById(R.id.webview_page_content);
        webView.getSettings().setJavaScriptEnabled(true);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        progressPage = (ProgressBar) findViewById(R.id.progress_page);
        progressPage.setMax(comicPackage.getNumberOfPages());
    }

    private void updatePage() {

        if (textToSpeech != null)
            textToSpeech.stop();

        webView.loadDataWithBaseURL("", comicPackage.getPageText(comicPageId.getPageId()), "text/html", "UTF-8", "");


        progressPage.setProgress(comicPageId.getPageId() + 1);
    }

    @Override
    protected void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        webView.stopLoading();
        webView.loadUrl("");
        webView.reload();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_view);
        initializeComponents();
        fetchingPages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comic_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_next) {
            if (comicPageId.getPageId() < comicPackage.getNumberOfPages() - 1) {
                comicPageId.setPageId(comicPageId.getPageId() + 1);
                updatePage();
            }
            return true;
        } else if (id == R.id.action_previous) {
            if (comicPageId.getPageId() > 0) {
                comicPageId.setPageId(comicPageId.getPageId() - 1);
                updatePage();
            }
            return true;
        } else if (id == R.id.action_play) {
            if (textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            } else {
                Spanned text = Html.fromHtml(comicPackage.getPageText(comicPageId.getPageId()));
                textToSpeech.speak(text.toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
