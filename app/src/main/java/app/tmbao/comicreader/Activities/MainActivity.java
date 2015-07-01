package app.tmbao.comicreader.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.MatrixCursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import app.tmbao.comicreader.Library.ComicAchievement;
import app.tmbao.comicreader.Library.ComicAchievementManager;
import app.tmbao.comicreader.Library.ComicPackage;
import app.tmbao.comicreader.Library.ComicTitleCursorAdapter;
import app.tmbao.comicreader.Library.ComicUserManager;
import app.tmbao.comicreader.Library.ComicTitleItem;
import app.tmbao.comicreader.Library.ComicTitleArrayAdapter;
import app.tmbao.comicreader.Library.MediaHelper;
import app.tmbao.comicreader.R;


public class MainActivity extends Activity {

    private ListView listComic;
    private ArrayList<ComicTitleItem> comicTitleItems;
    private TextView achievementText;
    private ImageView achievementImage;
    private ProgressBar scoreProgress;

    private Menu menu;

//    Facebook SDK
    private CallbackManager callbackManager;

//    Facebook UI
    private TextView userNameText;
    private ProfilePictureView profilePictureView;
    private LoginButton loginButton;

    private ComicAchievementManager comicAchievementManager;
    private ComicUserManager comicUserManager;

    private void updateScore() {
//
//        Set score
        scoreProgress.setProgress(comicUserManager.getScore());
        ComicAchievement currentAchievement = comicAchievementManager.getAchievement(comicUserManager.getScore());
        ComicAchievement nextAchievement = comicAchievementManager.getNextAchievement(comicUserManager.getScore());
        scoreProgress.setMax(nextAchievement.getRequiredScore() - currentAchievement.getRequiredScore());
        scoreProgress.setProgress(comicUserManager.getScore() - currentAchievement.getRequiredScore());

//        Set title
        achievementText.setText(currentAchievement.getLevelName());
        achievementImage.setImageBitmap(currentAchievement.getLevelImage(this));
    }

    private void fetchingAllComics() {
        comicTitleItems = MediaHelper.getAllComicTitle();

//        Load record
        comicUserManager = ComicUserManager.getInstance();
        comicUserManager.load(this);
//        Load achievements
        comicAchievementManager = ComicAchievementManager.getInstance();
        comicAchievementManager.load(this);
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

    private void setProfile(Profile profile) {
        if (profile != null) {
            profilePictureView.setProfileId(profile.getId());
            userNameText.setText(profile.getName());
        } else {
            userNameText.setText("Default User");
            profilePictureView.setProfileId(null);
        }
    }

    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "app.tmbao.comicreader", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
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

        profilePictureView = (ProfilePictureView) findViewById(R.id.profile_picture);
        userNameText = (TextView) findViewById(R.id.text_user_name);

        callbackManager = CallbackManager.Factory.create();


        loginButton = (LoginButton) findViewById(R.id.button_login_facebook);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setProfile(Profile.getCurrentProfile());
            }

            @Override
            public void onCancel() {
                setProfile(Profile.getCurrentProfile());
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        scoreProgress = (ProgressBar) findViewById(R.id.progress_score);
        achievementText = (TextView) findViewById(R.id.text_achievement);
        achievementImage = (ImageView) findViewById(R.id.image_achievement);

        setProfile(Profile.getCurrentProfile());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        ComicUserManager.getInstance().saveRecord();
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
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        initializeComponents();
        fetchingAllComics();
        initializeListComicTitle();
    }

    private void loadSuggestions(String text) {
        ArrayList<ComicTitleItem> suggItems = new ArrayList<ComicTitleItem>();
        for (int index = 0; index < comicTitleItems.size(); index++)
            if (comicTitleItems.get(index).getTitle().contains(text)) {
                suggItems.add(comicTitleItems.get(index));
            }
        listComic.setAdapter(new ComicTitleArrayAdapter(this, suggItems));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();

        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadSuggestions(newText);
                return true;
            }
        });

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
