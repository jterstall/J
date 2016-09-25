package terstall.jeroenterstall_pset3;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

// This class overwrites the basic adapter for a listview with images, text and a button

public class CustomAdapter extends BaseAdapter implements ListAdapter
{
    private ArrayList<String> watchlist;
    private ArrayList<String> poster_urls;
    Context context;

    // Constructor
    public CustomAdapter(ArrayList<String> watchlist, ArrayList<String> poster_urls, Context context)
    {
        this.watchlist = watchlist;
        this.poster_urls = poster_urls;
        this.context = context;
    }

    // Stays the same function
    @Override
    public int getCount()
    {
        return watchlist.size();
    }

    // Stays the same function
    @Override
    public Object getItem(int position)
    {
        return watchlist.get(position);
    }

    // Stays the same function
    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // Create a listview with custom list items made in xml
        View view = convertView;
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_1, null);
        }

        // Retrieve TextView object and change the text to the movie title
        TextView listItem = (TextView) view.findViewById(R.id.movie_name);
        listItem.setText(watchlist.get(position).toString());

        // Retrieve ImageView for the poster image and set it with the correct image
        ImageView posterImage = (ImageView) view.findViewById(R.id.poster_image);
        String poster_url = poster_urls.get(position).toString();
        if(poster_url.equals("N/A"))
        {
            posterImage.setImageResource(R.drawable.no_image);
        }
        else
        {
            new ShowPosterTask(posterImage).execute(poster_url);
        }

        // Retrieve delete button image view
        ImageView deleteButton = (ImageView) view.findViewById(R.id.delete_button);
        // If button is clicked, ask the user for confirmation and call the removeItem function in
        // WatchListActivity class
        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Retrieve activity from context
                final WatchListActivity activity = (WatchListActivity) context;
                // Create the confirmation message
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Do you really want to remove this movie from your Watch List?");
                builder.setTitle("Confirmation");
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener()
                {
                    // If the user decides to delete call removeItem from WatchListActivity class
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        activity.removeItem(context, watchlist.get(position).toString());
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
                {
                    // Otherwise do nothing
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        System.out.println("CANCEL");
                    }
                });
                builder.show();
            }
        });
        return view;
    }
}
