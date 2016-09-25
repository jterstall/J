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


public class CustomAdapter extends BaseAdapter implements ListAdapter
{
    private ArrayList<String> watchlist;
    private ArrayList<String> poster_urls;
    Context context;

    public CustomAdapter(ArrayList<String> watchlist, ArrayList<String> poster_urls, Context context)
    {
        this.watchlist = watchlist;
        this.poster_urls = poster_urls;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return watchlist.size();
    }

    @Override
    public Object getItem(int position)
    {
        return watchlist.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_1, null);
        }

        TextView listItem = (TextView) view.findViewById(R.id.movie_name);
        listItem.setText(watchlist.get(position).toString());

        ImageView deleteButton = (ImageView) view.findViewById(R.id.delete_button);

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

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final WatchListActivity activity = (WatchListActivity) context;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Do you really want to remove this movie from your Watch List?");
                builder.setTitle("Confirmation");
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        activity.removeItem(context, watchlist.get(position).toString());
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
                {
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
