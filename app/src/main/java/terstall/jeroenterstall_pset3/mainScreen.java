package terstall.jeroenterstall_pset3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectedListener;

// Class which handles the first screen the users see

public class mainScreen extends AppCompatActivity
{
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        // Set the bottom bar
        setBottomBar(savedInstanceState, 1);
    }

    // Make sure the bottombar is at the correct tab when back button is pressed
    @Override
    protected void onRestart()
    {
        super.onRestart();
        if(bottomBar != null)
        {
            bottomBar.selectTabAtPosition(1, true);
        }
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
