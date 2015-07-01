package app.tmbao.comicreader.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

import app.tmbao.comicreader.Library.ComicPackage;
import app.tmbao.comicreader.Library.ComicPageIdItem;
import app.tmbao.comicreader.R;

public class ComicViewActivity extends Activity {

    TextToSpeech textToSpeech;

    ComicPackage comicPackage;
    ComicPageIdItem comicPageId;

    ImageButton nextButton;
    ImageButton previousButton;
    ImageButton playButton;
    TextView currentPageIdText;
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

        nextButton = (ImageButton) findViewById(R.id.button_next);
        previousButton = (ImageButton) findViewById(R.id.button_previous);
        playButton = (ImageButton) findViewById(R.id.button_play);
        currentPageIdText = (TextView) findViewById(R.id.text_current_page_id);
        webView = (WebView) findViewById(R.id.webview_page_content);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comicPageId.setPageId(comicPageId.getPageId() + 1);
                updatePage();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comicPageId.setPageId(comicPageId.getPageId() - 1);
                updatePage();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spanned text = Html.fromHtml(comicPackage.getPageText(comicPageId.getPageId()));
                textToSpeech.speak(text.toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
    }

    private void updatePage() {

        if (textToSpeech != null)
            textToSpeech.stop();

        webView.loadDataWithBaseURL("", comicPackage.getPageText(comicPageId.getPageId()), "text/html", "UTF-8", "");

//        Start page
        if (comicPageId.getPageId() == 0)
            previousButton.setEnabled(false);
        else
            previousButton.setEnabled(true);

//        Last page
        if (comicPageId.getPageId() == comicPackage.numberOfPages() - 1)
            nextButton.setEnabled(false);
        else
            nextButton.setEnabled(true);

        currentPageIdText.setText("Page " + String.valueOf(comicPageId.getPageId()) + "/" + String.valueOf(comicPackage.numberOfPages()));
    }

    @Override
    protected void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
