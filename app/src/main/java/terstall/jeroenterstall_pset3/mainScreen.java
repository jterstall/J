package terstall.jeroenterstall_pset3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectedListener;

public class mainScreen extends AppCompatActivity
{
    BottomBar bottomBar;
    Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        setBottomBar(savedInstanceState, 1);
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        if(bottomBar != null)
        {
            bottomBar.selectTabAtPosition(1, true);
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

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (bottomBar != null)
        {
            bottomBar.onSaveInstanceState(outState);
        }
    }

}
