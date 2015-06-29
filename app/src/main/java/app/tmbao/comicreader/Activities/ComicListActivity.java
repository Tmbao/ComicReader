package app.tmbao.comicreader.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.tmbao.comicreader.Library.ComicPackage;
import app.tmbao.comicreader.Library.ComicRecord;
import app.tmbao.comicreader.Library.ComicTitleItem;
import app.tmbao.comicreader.Library.ComicTitleArrayAdapter;
import app.tmbao.comicreader.Library.MediaHelper;
import app.tmbao.comicreader.R;


public class ComicListActivity extends Activity {

    private ListView listComic;
    private ArrayList<ComicTitleItem> comicTitleItems;
    private TextView scoreTextView;

    private void updateScore() {
        scoreTextView.setText("Score: " + String.valueOf(ComicRecord.getInstance().getScore()));
    }

    private void fetchingAllComics() {
        comicTitleItems = MediaHelper.getAllComicTitle();
        ComicRecord.getInstance().loadRecord(this);
    }

    private void initializeListComicTitle() {
        ComicTitleArrayAdapter adapter = new ComicTitleArrayAdapter(this, comicTitleItems);
        listComic.setAdapter(adapter);
    }

    private void showComicPreview(ComicTitleItem comicTitleItem) {
//        Start background worker
        ComicPackage comicPackage = ComicPackage.getInstance();
        comicPackage.setComic(comicTitleItem);

//        Show ComicPreviewActivity
        Intent intent = new Intent(this, ComicPreviewActivity.class);
        startActivity(intent);
    }

    private void initializeComponents() {
        listComic = (ListView) findViewById(R.id.list_comic);
        listComic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ComicTitleItem selectedComicTitleItem = (ComicTitleItem) parent.getItemAtPosition(position);
                showComicPreview(selectedComicTitleItem);
            }
        });
        scoreTextView = (TextView) findViewById(R.id.text_score);
    }

    @Override
    protected void onPause() {
        ComicRecord.getInstance().saveRecord();
        super.onPause();
    }

    @Override
    protected void onResume() {
        updateScore();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_title);

        initializeComponents();
        fetchingAllComics();
        initializeListComicTitle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
