package app.tmbao.comicreader.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import app.tmbao.comicreader.Library.ComicAchievement;
import app.tmbao.comicreader.Library.ComicPackage;
import app.tmbao.comicreader.Library.ComicQuestion;
import app.tmbao.comicreader.Library.ComicQuestionOptionArrayAdapter;
import app.tmbao.comicreader.Library.ComicUserManager;
import app.tmbao.comicreader.R;

public class ComicQuestionActivity extends Activity implements ComicUserManager.AchievementCallback {

    WebView questionStatement;
    ListView questionOptions;

    ComicPackage comicPackage;
    ComicUserManager comicUserManager;
    int currentQuestionId;

    private boolean showQuestion(int currentQuestionId) {
        if (currentQuestionId >= comicPackage.numberOfQuestions())
            return false;

        ComicQuestion question = comicPackage.getQuestion(currentQuestionId);
        questionStatement.loadDataWithBaseURL("", question.getQuestionStatement(), "text/html", "UTF-8", "");

        ComicQuestionOptionArrayAdapter adapter = new ComicQuestionOptionArrayAdapter(this, question.getOptions());
        questionOptions.setAdapter(adapter);

        getActionBar().setTitle("Question " + String.valueOf(currentQuestionId + 1) + "/" + String.valueOf(comicPackage.numberOfQuestions()));
        return true;
    }

    private void fetchingAllQuestions() {
        comicPackage = ComicPackage.getInstance();
        comicUserManager = ComicUserManager.getInstance();
        comicUserManager.setAchievementCallback(this);
        currentQuestionId = 0;
    }

    private boolean verifyAnswer(int selectedOption) {
        if (comicPackage.getQuestion(currentQuestionId).getCorrectOption() == selectedOption) {
            Toast.makeText(getApplicationContext(), "Your answer is correct!", Toast.LENGTH_SHORT).show();
            comicUserManager.updateQuestion(this, comicPackage.getTitle() + "_" + comicPackage.getQuestion(currentQuestionId).getAlias());
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Your answer is wrong!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void showHint() {
        ComicPackage comicPackage = ComicPackage.getInstance();
        ComicUserManager.getInstance().answerQuestion(comicPackage.getTitle() + "_" + comicPackage.getQuestion(currentQuestionId).getAlias());
        String hint = comicPackage.getQuestion(currentQuestionId).getOptions().get(comicPackage.getQuestion(currentQuestionId).getCorrectOption());

        Toast.makeText(this, hint, Toast.LENGTH_SHORT).show();
    }

    private void initializeComponents() {
        questionStatement = (WebView) findViewById(R.id.webview_question_statement);
        questionStatement.getSettings().setJavaScriptEnabled(true);
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
    protected void onPause() {
        questionStatement.stopLoading();
        questionStatement.loadUrl("");
        questionStatement.reload();

        comicUserManager.saveRecord();

        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_question);

        initializeComponents();
        fetchingAllQuestions();

        if (comicPackage.numberOfQuestions() == 0)
            finish();

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
        if (id == R.id.action_hint) {
            showHint();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewAchievementUnlocked(final ComicAchievement achievement) {
//        Play music
        if (Profile.getCurrentProfile() != null) {
            MainActivity.updateShareContent(achievement);

            SharePhoto photo = new SharePhoto.Builder().setBitmap(achievement.getLevelImageLarge(this)).build();
            final SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();

            final Activity activity = this;

            new AlertDialog.Builder(this)
                    .setTitle("Congratulation")
                    .setMessage("New achievement unlocked, do you want to share with your friends?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ShareDialog.show(activity, content);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(achievement.getLevelImageId(this))
                    .show();
        }
    }
}
