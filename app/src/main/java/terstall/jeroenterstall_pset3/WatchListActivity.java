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
        setBottomBar(savedInstanceState, 2);
        retrieveWatchlistedMovies();
        populateList();
    }

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

    private void retrieveWatchlistedMovies()
    {
        watchlist.clear();
        watchlist_posters.clear();
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

    private void populateList()
    {
        lv = (ListView) findViewById(R.id.watchlist);
        CustomAdapter adapter = new CustomAdapter(watchlist, watchlist_posters, this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String title = lv.getItemAtPosition(position).toString();
                title = title.replaceAll("\\s+", "+");
                Intent intent = new Intent(getApplicationContext(), ShowMovieInformation.class);
                try
                {
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

    public void removeItem(Context context, String title)
    {
        sp = getSharedPreferences(MOVIES_PREF, 0);
        sp_posters = getSharedPreferences(MOVIES_PREF_POSTER, 0);
        editor = sp.edit();
        editor_posters = sp_posters.edit();

        editor.remove(title);
        editor_posters.remove(title);
        editor.commit();
        editor_posters.commit();

        Toast toast = Toast.makeText(context, "Removed from Watch List", Toast.LENGTH_SHORT);
        toast.show();

        retrieveWatchlistedMovies();
        populateList();
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

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (bottomBar != null)
        {
            bottomBar.onSaveInstanceState(outState);
        }
    }

}
