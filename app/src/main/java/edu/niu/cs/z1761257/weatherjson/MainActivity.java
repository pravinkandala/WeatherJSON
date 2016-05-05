package edu.niu.cs.z1761257.weatherjson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private List<Weather> weatherList = new ArrayList<>();
    private WeatherArrayAdapter weatherArrayAdapter;
    private ListView weatherListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherListView = (ListView)findViewById(R.id.weatherListView);
        weatherArrayAdapter = new WeatherArrayAdapter(this, weatherList);
        weatherListView.setAdapter(weatherArrayAdapter);
    }//end onCreate


    private class GetWeatherTask extends AsyncTask<URL,String,JSONObject>
    {
        @Override
        protected JSONObject doInBackground(URL... params)
        {
            HttpURLConnection connection = null;

            try
            {
                connection = (HttpURLConnection)params[0].openConnection();
                int response = connection.getResponseCode();
                if( response == HttpURLConnection.HTTP_OK )
                {
                    StringBuilder builder = new StringBuilder();
                    try
                    {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        while( (line = reader.readLine()) != null)
                            builder.append(line);
                    }
                    catch(Exception e)
                    {
                        publishProgress(getString(R.string.read_error));
                        e.printStackTrace();
                    }

                    return new JSONObject(builder.toString());
                }//end if
                else
                {
                    publishProgress(getString(R.string.connect_error));
                }
            }
            catch (Exception e)
            {
                publishProgress(getString(R.string.connect_error));
                e.printStackTrace();
            }
            finally
            {
                connection.disconnect();
            }

            return null;
        }//end doInBackground

        @Override
        protected void onProgressUpdate(String... values)
        {
            Toast.makeText(MainActivity.this, values[0],Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject)
        {
            convertJSONtoArrayList(jsonObject);
            weatherArrayAdapter.notifyDataSetChanged();
            weatherListView.smoothScrollToPosition(0);
        }//end onPostExecute
    }//end GetWeatherTask

    private void convertJSONtoArrayList( JSONObject jsonObject )
    {
    }//end convertJSONtoArrayList
}//end MainActivity
