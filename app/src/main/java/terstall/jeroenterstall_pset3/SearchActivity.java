package terstall.jeroenterstall_pset3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity
{
    // Intent variable names
    private static final String MOVIE_INFORMATION = "MOVIE_INFORMATION";

    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setBottomBar(savedInstanceState, 0);
        EditText searchBar = (EditText) findViewById(R.id.movieTitle);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    displayMovieInformation(v);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        if(bottomBar != null)
        {
            bottomBar.selectTabAtPosition(0, true);
        }
    }

    private void setBottomBar(Bundle savedInstanceState, int defaultPosition)
    {
        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItems(
                new BottomBarTab(R.drawable.ic_search_white_24dp, "Search"),
                new BottomBarTab(R.drawable.ic_home_white_24dp, "Home"),
                new BottomBarTab(R.drawable.ic_visibility_white_24dp, "Watch List")
        );
        bottomBar.useDarkTheme(true);
        bottomBar.setActiveTabColor('w');
        bottomBar.selectTabAtPosition(defaultPosition, false);
        bottomBar.setOnItemSelectedListener(new OnTabSelectedListener()
        {
            @Override
            public void onItemSelected(int position)
            {
                Intent intent;
                switch(position)
                {
                    case 0:
                        intent = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), mainScreen.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), WatchListActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    public void displayMovieInformation(View v)
    {
        EditText movieTitle = (EditText) findViewById(R.id.movieTitle);
        String title = movieTitle.getText().toString();
        movieTitle.setText("");
        if(title.trim().length() != 0)
        {
            Intent intent = new Intent(this, ShowMovieInformation.class);
            title = title.replaceAll("\\s+", "+");
            String movieInformation = retrieveMovieInformation(title);
            try
            {
                JSONObject jsonObject = new JSONObject(movieInformation);
                String response = jsonObject.getString(RetrieveMovieInformationTask.JSON_RESPONSE);
                if(response.equals("False"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "No movie found...", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    intent.putExtra(MOVIE_INFORMATION, movieInformation);
                    startActivity(intent);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Input a movie title", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private String retrieveMovieInformation (String movieTitle)
    {
        try
        {
            String movieInformation = new RetrieveMovieInformationTask().execute(movieTitle).get();
            return movieInformation;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (bottomBar != null)
        {
            bottomBar.onSaveInstanceState(outState);
        }
    }
}
