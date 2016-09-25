package terstall.jeroenterstall_pset3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowMovieInformation extends AppCompatActivity
{
    // Intent variable names
    private static final String MOVIE_INFORMATION = "MOVIE_INFORMATION";
    private static final String MOVIES_PREF = "MOVIES";
    private static final String MOVIES_PREF_POSTER = "MOVIES_POSTER";

    // Movie information variables
    String title;
    String year;
    String director;
    String writer;
    String runtime;
    String genre;
    String plot;
    String actors;
    String imdbRating;
    String tomatoesRating;
    String metascore;
    String poster;

    // Shared preferences and corresponding editors
    SharedPreferences sp;
    SharedPreferences sp_posters;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_posters;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movie_information);
        // Retrieve movie information from intent
        Intent intent = this.getIntent();
        String movieInformation = intent.getStringExtra(MOVIE_INFORMATION);
        // Display that information
        displayMovieInformation(movieInformation);
        // set an icon based on if the movie is watchlisted
        setWatchListIcon(movieInformation);
    }

    // This functions sets all views according to movie information from JSON
    private void displayMovieInformation(String movieInformation)
    {
        try
        {
            // Create JSON object from string
            JSONObject jsonObject = new JSONObject(movieInformation);

            // Retrieve movie information from json
            title = jsonObject.getString(RetrieveMovieInformationTask.JSON_TITLE);
            year = "(" + jsonObject.getString(RetrieveMovieInformationTask.JSON_YEAR) + ")";
            director = "<b> Staff: </b> <br> Directed by: " + jsonObject.getString(RetrieveMovieInformationTask.JSON_DIRECTOR);
            writer = "Written by: " + jsonObject.getString(RetrieveMovieInformationTask.JSON_WRITER);
            runtime = jsonObject.getString(RetrieveMovieInformationTask.JSON_RUNTIME);
            genre = jsonObject.getString(RetrieveMovieInformationTask.JSON_GENRE);
            plot = "<b> Plot: </b> <br>" + jsonObject.getString(RetrieveMovieInformationTask.JSON_PLOT);
            actors = "Stars: " + jsonObject.getString(RetrieveMovieInformationTask.JSON_ACTORS);
            imdbRating = "<b> Ratings: </b> <br> IMDB: " + jsonObject.getString(RetrieveMovieInformationTask.JSON_IMDB_SCORE);
            tomatoesRating = "Rotten Tomatoes: " + jsonObject.getString(RetrieveMovieInformationTask.JSON_TOMATO_SCORE);
            metascore = "Metascore: " + jsonObject.getString(RetrieveMovieInformationTask.JSON_META_SCORE) + "/100";
            poster = jsonObject.getString(RetrieveMovieInformationTask.JSON_POSTER);

            // Show Poster
            if(poster.equals("N/A"))
            {
                ImageView poster_image = (ImageView) findViewById(R.id.posterImage);
                poster_image.setImageResource(R.drawable.no_image);
            }
            else
            {
                new ShowPosterTask((ImageView) findViewById(R.id.posterImage)).execute(poster);
            }

            // Retrieve TextView ids
            TextView movieTitle = (TextView) findViewById(R.id.movie_title);
            TextView moviePlot = (TextView) findViewById(R.id.movie_plot);
            TextView rating_metascore = (TextView) findViewById(R.id.movie_rating_metascore);
            TextView rating_rottentomatoes = (TextView) findViewById(R.id.movie_rating_rottentomatoes);
            TextView rating_imdb = (TextView) findViewById(R.id.movie_rating_imdb);
            TextView movieDirector = (TextView) findViewById(R.id.movie_director);
            TextView movieWriter = (TextView) findViewById(R.id.movie_writer);
            TextView movieActors = (TextView) findViewById(R.id.movie_actors);
            TextView movieGenre_Runtime = (TextView) findViewById(R.id.movie_genre_runtime);

            // Set TextViews to corresponding movie information
            movieDirector.setText(Html.fromHtml(director));
            rating_imdb.setText(Html.fromHtml(imdbRating));
            moviePlot.setText(Html.fromHtml(plot));
            movieWriter.setText(writer);
            movieActors.setText(actors);
            movieTitle.setText(title + " " + year);
            movieGenre_Runtime.setText(genre + ", " + runtime);
            rating_metascore.setText(metascore);
            rating_rottentomatoes.setText(tomatoesRating);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    // Function that gets called if a movie is added to watchlist
    public void addToWatchList(View v)
    {
        // Retrieve watchlist icon view
        ImageView watchlist_icon = (ImageView) findViewById(R.id.watchlist_icon);

        // Retrieve movie information from intent
        Intent intent = getIntent();
        String movieInformation = intent.getStringExtra(MOVIE_INFORMATION);

        try
        {
            // Retrieve title and poster url from JSON
            JSONObject jsonObject = new JSONObject(movieInformation);
            title = jsonObject.getString(RetrieveMovieInformationTask.JSON_TITLE);
            poster = jsonObject.getString(RetrieveMovieInformationTask.JSON_POSTER);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        // Retrieve sharedpreferences and correponding editors
        sp = getApplicationContext().getSharedPreferences(MOVIES_PREF, 0);
        sp_posters = getApplicationContext().getSharedPreferences(MOVIES_PREF_POSTER, 0);
        editor = sp.edit();
        editor_posters = sp_posters.edit();

        // If the movie is already watchlisted, remove the movie from watchlist
        if(sp.contains(title))
        {
            // Remove from both editors
            editor.remove(title);
            editor_posters.remove(title);
            editor.commit();
            editor_posters.commit();
            // Change icon
            watchlist_icon.setImageResource(R.drawable.ic_add_box_white_24dp);
            // Tell the user what happened
            Toast toast = Toast.makeText(getApplicationContext(), "Removed from Watch List", Toast.LENGTH_SHORT);
            toast.show();
        }
        // If the movie is not watchlisted, add the movie to watchlist
        else
        {
            // Add to editors
            editor.putString(title, title);
            editor_posters.putString(title, poster);
            editor.commit();
            editor_posters.commit();
            // Change icon
            watchlist_icon.setImageResource(R.drawable.ic_check_box_white_24dp);
            // Tell the user what happened
            Toast toast = Toast.makeText(getApplicationContext(), "Added to Watch List!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    // Function to set the icon under the movie poster, to remove/add movie to watchlist
    private void setWatchListIcon(String movieInformation)
    {
        try
        {
            // Retrieve title from JSON
            JSONObject jsonObject = new JSONObject(movieInformation);
            title = jsonObject.getString(RetrieveMovieInformationTask.JSON_TITLE);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        // Get ImageView from watchlist icon
        ImageView watchlist_icon = (ImageView) findViewById(R.id.watchlist_icon);
        // Retrieve sharedpreferences
        sp = getApplicationContext().getSharedPreferences(MOVIES_PREF, 0);
        // If movie title is in shared preferences, use the checkbox
        if(sp.contains(title))
        {
            watchlist_icon.setImageResource(R.drawable.ic_check_box_white_24dp);
        }
        // Otherwise use the add box
        else
        {
            watchlist_icon.setImageResource(R.drawable.ic_add_box_white_24dp);
        }
    }
}