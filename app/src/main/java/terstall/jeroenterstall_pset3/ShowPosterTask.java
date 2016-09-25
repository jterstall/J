package terstall.jeroenterstall_pset3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowPosterTask extends AsyncTask<String, Void, Bitmap>
{
    ImageView imageView;

    public ShowPosterTask(ImageView imageView)
    {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... params)
    {
        String image_url = params[0];
        URL url;
        try
        {
            url = new URL(image_url);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bmp;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Bitmap result)
    {
        imageView.setImageBitmap(result);
    }
}
