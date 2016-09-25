package terstall.jeroenterstall_pset3;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// This class handles the retrieving of json from the movie database API
// Also stores the JSON tags
public class RetrieveMovieInformationTask extends AsyncTask<String, Void, String>
{
    private static final String API_URL = "http://omdbapi.com/?";

    // Json names
    public static final String JSON_TITLE = "Title";
    public static final String JSON_YEAR = "Year";
    public static final String JSON_RUNTIME = "Runtime";
    public static final String JSON_GENRE = "Genre";
    public static final String JSON_DIRECTOR = "Director";
    public static final String JSON_WRITER = "Writer";
    public static final String JSON_ACTORS = "Actors";
    public static final String JSON_PLOT = "Plot";
    public static final String JSON_RESPONSE = "Response";
    public static final String JSON_IMDB_SCORE = "imdbRating";
    public static final String JSON_TOMATO_SCORE = "tomatoRating";
    public static final String JSON_META_SCORE = "Metascore";
    public static final String JSON_POSTER = "Poster";

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            // Retrieve movie title from arguments passed
            String title = params[0];
            // Create the url
            URL url = new URL(API_URL + "t=" + title + "&plot=full&type=movie&tomatoes=true");
            // Open a connection to api
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try
            {
                // Create a buffered reader to read in json
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                // While there are still lines to read, append to stringbuilder
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                reader.close();
                // Return the json
                return sb.toString();
            }
            finally
            {
                // At the end make sure to disconnect
                urlConnection.disconnect();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    @Override
    protected void onPostExecute(String result)
    {
        // Return result
        super.onPostExecute(result);
    }
}