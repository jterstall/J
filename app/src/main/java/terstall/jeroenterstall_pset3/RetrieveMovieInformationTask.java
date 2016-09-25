package terstall.jeroenterstall_pset3;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
            String title = params[0];
            URL url = new URL(API_URL + "t=" + title + "&plot=full&type=movie&tomatoes=true");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                reader.close();
                return sb.toString();
            }
            finally
            {
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
        super.onPostExecute(result);
    }
}