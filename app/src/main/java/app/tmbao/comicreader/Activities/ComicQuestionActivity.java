package app.tmbao.comicreader.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import app.tmbao.comicreader.Library.ComicPackage;
import app.tmbao.comicreader.Library.ComicQuestion;
import app.tmbao.comicreader.Library.ComicQuestionOptionArrayAdapter;
import app.tmbao.comicreader.Library.ComicRecord;
import app.tmbao.comicreader.R;

public class ComicQuestionActivity extends Activity {

    ImageView figure;
    TextView questionStatement;
    ListView questionOptions;

    ComicPackage comicPackage;
    ComicRecord comicRecord;
    int currentQuestionId;

    private boolean showQuestion(int currentQuestionId) {
        if (currentQuestionId >= comicPackage.numberOfQuestions())
            return false;

        ComicQuestion question = comicPackage.getQuestion(currentQuestionId);
        try {
            figure.setImageBitmap(question.getFigure());
        } catch (Exception e) {
            e.printStackTrace();
        }
        questionStatement.setText(question.getQuestionStatement());

        ComicQuestionOptionArrayAdapter adapter = new ComicQuestionOptionArrayAdapter(this, question.getOptions());
        questionOptions.setAdapter(adapter);

        getActionBar().setTitle("Question " + String.valueOf(currentQuestionId + 1) + "/" + String.valueOf(comicPackage.numberOfQuestions()));
        return true;
    }

    private void fetchingAllQuestions() {
        comicPackage = ComicPackage.getInstance();
        comicRecord = ComicRecord.getInstance();
        currentQuestionId = 0;
    }

    private boolean verifyAnswer(int selectedOption) {
        if (comicPackage.getQuestion(currentQuestionId).getCorrectOption() == selectedOption) {
            Toast.makeText(getApplicationContext(), "Your answer is correct!", Toast.LENGTH_SHORT).show();
            comicRecord.updateQuestion(comicPackage.getTitle() + "_" + comicPackage.getQuestion(currentQuestionId).getAlias());
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Your answer is wrong!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void initializeComponents() {
        figure = (ImageView) findViewById(R.id.image_question);
        questionStatement = (TextView) findViewById(R.id.text_question_statement);
        questionOptions = (ListView) findViewById(R.id.list_options);

        questionOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (verifyAnswer(position)) {
                    currentQuestionId++;
//                    No more question
                    if (!showQuestion(currentQuestionId)) {
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_question);

        initializeComponents();
        fetchingAllQuestions();

        showQuestion(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comic_question, menu);
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
