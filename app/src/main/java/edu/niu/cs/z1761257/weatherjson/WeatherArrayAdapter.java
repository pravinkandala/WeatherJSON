package edu.niu.cs.z1761257.weatherjson;

/**
 * Created by Pravin on 5/5/16.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter that will be used with the ListView
 */
public class WeatherArrayAdapter extends ArrayAdapter<Weather>
{
    //Hold the images with a description
    private Map<String,Bitmap> images = new HashMap<>();

    public WeatherArrayAdapter(Context context, List<Weather> objects)
    {
        super(context, -1, objects);
    }//end constructor

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Weather day = getItem(position);
        ViewHolder viewHolder;

        //If there is not a re-usable view, create one
        if( convertView == null )
        {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());

            //Attach the custom layout to the view from the ListView
            convertView = inflater.inflate(R.layout.list_item, parent, false );

            //Connect to the ListView item components
            viewHolder.conditionIV = (ImageView)convertView.findViewById(R.id.conditionImageVIew);
            viewHolder.dayTV = (TextView)convertView.findViewById(R.id.dayTextView);
            viewHolder.lowTV = (TextView)convertView.findViewById(R.id.lowTextView);
            viewHolder.highTV = (TextView)convertView.findViewById(R.id.highTextView);
            viewHolder.humidityTV = (TextView)convertView.findViewById(R.id.humidityTextView);

            //Store the ViewHolder with the ListView item
            convertView.setTag(viewHolder);
        }
        //Re-use an existing view
        else
        {
            //Get the re-usable view
            viewHolder = (ViewHolder)convertView.getTag();
        }


        return convertView;
    }//end getView


    //Class to hold the information for a ListView item
    private static class ViewHolder
    {
        ImageView conditionIV;
        TextView dayTV, lowTV, highTV, humidityTV;
    }//end ViewHolder

}//end WeatherArrayAdapter class
