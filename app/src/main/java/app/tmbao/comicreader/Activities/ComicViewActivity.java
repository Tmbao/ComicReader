package app.tmbao.comicreader.Activities;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import app.tmbao.comicreader.Library.ComicPackage;
import app.tmbao.comicreader.UI.ComicImageView;
import app.tmbao.comicreader.Library.ComicPageIdItem;
import app.tmbao.comicreader.R;

public class ComicViewActivity extends Activity {

    ComicPackage comicPackage;
    ComicPageIdItem comicPageId;

    ComicImageView currentPageView;
    Button nextButton, previousButton;
    TextView currentPageIdText;

    OrientationEventListener orientationEventListener;

    private void fetchingPages() {
        comicPackage = ComicPackage.getInstance();

        getActionBar().setTitle(comicPackage.getTitle());

        Bundle bundle = getIntent().getBundleExtra("ComicPageId");
//        Set currentPageView
        if (bundle != null)
            comicPageId = new ComicPageIdItem(bundle);
        else
            comicPageId = new ComicPageIdItem(0);

//        Jump to pageId
        currentPageView.setImageBitmap(comicPackage.getPage(comicPageId.getPageId()));
    }

    void updateOrientation(int orientation) {
        currentPageView.setImageBitmap(comicPackage.getPage(comicPageId.getPageId()));
    }

    private void initializeComponents() {
        currentPageView = (ComicImageView) findViewById(R.id.image_current_page);
        nextButton = (Button) findViewById(R.id.button_next);
        previousButton = (Button) findViewById(R.id.button_previous);
        currentPageIdText = (TextView) findViewById(R.id.text_current_page_id);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comicPageId.setPageId(comicPageId.getPageId() + 1);
                currentPageView.setImageBitmap(comicPackage.getPage(comicPageId.getPageId()));
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comicPageId.setPageId(comicPageId.getPageId() - 1);
                currentPageView.setImageBitmap(comicPackage.getPage(comicPageId.getPageId()));
            }
        });

        currentPageView.setOnSetImageListener(new ComicImageView.OnSetImageListener() {
            @Override
            public void imageChanged() { // Page change
//                Start page
                if (comicPageId.getPageId() == 0)
                    previousButton.setEnabled(false);
                else
                    previousButton.setEnabled(true);

//                Last page
                if (comicPageId.getPageId() == comicPackage.numberOfPages() - 1)
                    nextButton.setEnabled(false);
                else
                    nextButton.setEnabled(true);

                currentPageIdText.setText("Page " + String.valueOf(comicPageId.getPageId()) + "/" + String.valueOf(comicPackage.numberOfPages()));
            }
        });

        orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                updateOrientation(orientation);
            }
        };
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
