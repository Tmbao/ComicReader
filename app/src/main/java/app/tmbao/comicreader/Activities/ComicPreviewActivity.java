package app.tmbao.comicreader.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import app.tmbao.comicreader.Library.ComicPackage;
import app.tmbao.comicreader.Library.ComicPageIdArrayAdapter;
import app.tmbao.comicreader.Library.ComicPageIdItem;
import app.tmbao.comicreader.R;

public class ComicPreviewActivity extends Activity {

    private ListView listPageId;
    private ArrayList<ComicPageIdItem> pageIdItems;
    private ComicPackage comicPackage;

    private void fetchingAllPages() {
        comicPackage = ComicPackage.getInstance();

        getActionBar().setTitle(comicPackage.getTitle());

        pageIdItems = new ArrayList<>();
        int numberOfPages = comicPackage.getNumberOfPages();
        for (int index = 0; index < numberOfPages; index++)
            pageIdItems.add(new ComicPageIdItem(index));
    }

    private void initializeListComicPageId() {
        ComicPageIdArrayAdapter adapter = new ComicPageIdArrayAdapter(this, pageIdItems);
        listPageId.setAdapter(adapter);
    }

    private void showAllPage(ComicPageIdItem comicPageIdItem) {
//        Stop background worker
        ComicPackage comicPackage = ComicPackage.getInstance();

//        Show ComicViewActivity
        Intent intent = new Intent(this, ComicViewActivity.class);
        intent.putExtra("ComicPageId", comicPageIdItem.toBundle());
        startActivity(intent);
    }

    private void showQuestions() {
        Intent intent = new Intent(this, ComicQuestionActivity.class);
        startActivity(intent);
    }

    private void initializeComponents() {
        listPageId = (ListView) findViewById(R.id.list_page_id);
        listPageId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ComicPageIdItem selectedComicPageIdItem = (ComicPageIdItem) parent.getItemAtPosition(position);
                showAllPage(selectedComicPageIdItem);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_preview);

        initializeComponents();
        fetchingAllPages();
        initializeListComicPageId();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comic_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_question) {
            showQuestions();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
