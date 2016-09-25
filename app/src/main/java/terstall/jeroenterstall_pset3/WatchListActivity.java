package terstall.jeroenterstall_pset3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectedListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

// This class handles the watch list and corresponding activities

public class WatchListActivity extends AppCompatActivity
{
    private static final String MOVIE_INFORMATION = "MOVIE_INFORMATION";
    private static final String MOVIES_PREF_POSTER = "MOVIES_POSTER";
    private static final String MOVIES_PREF = "MOVIES";

    BottomBar bottomBar;

    ListView lv;

    SharedPreferences sp;
    SharedPreferences sp_posters;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_posters;

    ArrayList<String> watchlist = new ArrayList<String>();
    ArrayList<String> watchlist_posters = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);
        // Init bottom bar
        setBottomBar(savedInstanceState, 2);
        // Fill array lists with shared preference information
        retrieveWatchlistedMovies();
        // Populate listview
        populateList();
    }

    // Make sure the bottombar is on the right tab when the back button is pressed
    @Override
    protected void onRestart()
    {
        super.onRestart();
        retrieveWatchlistedMovies();
        populateList();
        if(bottomBar != null)
        {
            bottomBar.selectTabAtPosition(2, true);
        }
    }

    // Function retrieves information from shared preference and adds it to the array lists
    private void retrieveWatchlistedMovies()
    {
        // Clear previously filled arraylists
        watchlist.clear();
        watchlist_posters.clear();

        // Retrieve posters and movie titles and store in array list
        sp = getSharedPreferences(MOVIES_PREF, 0);
        Map<String, ?> movies = sp.getAll();
        for (Map.Entry<String, ?> entry : movies.entrySet())
        {
            watchlist.add(entry.getValue().toString());
        }
        sp = getSharedPreferences(MOVIES_PREF_POSTER, 0);
        Map<String, ?> movies_poster = sp.getAll();
        for (Map.Entry<String, ?> entry: movies_poster.entrySet())
        {
            watchlist_posters.add(entry.getValue().toString());
        }
    }

    // This function creates the list view and adds the movies to it
    private void populateList()
    {
        // Find listview
        lv = (ListView) findViewById(R.id.watchlist);
        // Create the custom adapter with an image, movie title and delete butotn
        CustomAdapter adapter = new CustomAdapter(watchlist, watchlist_posters, this);
        lv.setAdapter(adapter);
        // Listen for clicks on the list which should take you to the movie information page
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // Retrieve movie title
                String title = lv.getItemAtPosition(position).toString();
                // Ready for pasting in URL
                title = title.replaceAll("\\s+", "+");
                // Init intent
                Intent intent = new Intent(getApplicationContext(), ShowMovieInformation.class);
                try
                {
                    // Retrieve movie information and start the next activity with that added to it
                    String movieInformation = new RetrieveMovieInformationTask().execute(title).get();
                    intent.putExtra(MOVIE_INFORMATION, movieInformation);
                    startActivity(intent);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                catch (ExecutionException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    // Function when the delete button is pressed
    public void removeItem(Context context, String title)
    {
        // Get shared preferences and editors
        sp = getSharedPreferences(MOVIES_PREF, 0);
        sp_posters = getSharedPreferences(MOVIES_PREF_POSTER, 0);
        editor = sp.edit();
        editor_posters = sp_posters.edit();

        // Remove correct title from both shared preferences
        editor.remove(title);
        editor_posters.remove(title);
        editor.commit();
        editor_posters.commit();

        // Show a message that the movie is deleted from the watch list
        Toast toast = Toast.makeText(context, "Removed from Watch List", Toast.LENGTH_SHORT);
        toast.show();

        // Re-populate the watchlist without the removed movie
        retrieveWatchlistedMovies();
        populateList();
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
