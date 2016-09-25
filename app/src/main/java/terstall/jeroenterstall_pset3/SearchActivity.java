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

// Class handles the searching of movies
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
        // Set bottom bar
        setBottomBar(savedInstanceState, 0);
        // Retrieve search bar and add a listener so the user can push enter to search
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

    // Make sure the bottom bar is at the correct tab when back button is pressed
    @Override
    protected void onRestart()
    {
        super.onRestart();
        if(bottomBar != null)
        {
            bottomBar.selectTabAtPosition(0, true);
        }
    }

    // This function retrieves the movie information and sends it to the display movie information activity
    public void displayMovieInformation(View v)
    {
        // Retrieve title entered by user and clean
        EditText movieTitle = (EditText) findViewById(R.id.movieTitle);
        String title = movieTitle.getText().toString();
        movieTitle.setText("");
        // If the user filled something in
        if(title.trim().length() != 0)
        {
            // Init intent
            Intent intent = new Intent(this, ShowMovieInformation.class);
            // Replace spaces with +'s which is needed in the url
            title = title.replaceAll("\\s+", "+");
            // Retrieve movie information
            String movieInformation = retrieveMovieInformation(title);
            try
            {
                // Turn string into JSON
                JSONObject jsonObject = new JSONObject(movieInformation);
                // CHeck if a movie was found
                String response = jsonObject.getString(RetrieveMovieInformationTask.JSON_RESPONSE);
                // If no movie was found, tell the user so
                if(response.equals("False"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "No movie found...", Toast.LENGTH_SHORT);
                    toast.show();
                }
                // Otherwise go to next activity to display movie information
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
        // If the user did not input a title, tell the user so
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Input a movie title", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // Function to retrieve the json from a movie title
    private String retrieveMovieInformation (String movieTitle)
    {
        try
        {
            // Retrieve movie information with the use of an AsyncTask
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

    // Sets the bottombar
    private void setBottomBar(Bundle savedInstanceState, int defaultPosition)
    {
        // Attach bottombar to current activity
        bottomBar = BottomBar.attach(this, savedInstanceState);

        // Fill with items
        bottomBar.setItems(
                new BottomBarTab(R.drawable.ic_search_white_24dp, "Search"),
                new BottomBarTab(R.drawable.ic_home_white_24dp, "Home"),
                new BottomBarTab(R.drawable.ic_visibility_white_24dp, "Watch List")
        );

        // Some layout changes
        bottomBar.useDarkTheme(true);
        bottomBar.setActiveTabColor('w');
        bottomBar.selectTabAtPosition(defaultPosition, false);

        // Listen for clicks on bottombar to switch between activities
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

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        // Save bottombar with orientation changes
        if (bottomBar != null)
        {
            bottomBar.onSaveInstanceState(outState);
        }
    }
}
