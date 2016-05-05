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
import java.io.InputStream;
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
        weatherList.clear();

        try{
            JSONArray list = jsonObject.getJSONArray("list");
            for (int i=0; i<list.length();i++){

                JSONObject dayObj = list.getJSONObject(i),
                        tempObj = dayObj.getJSONObject("temp"),
                        weatherObj = dayObj.getJSONArray("weather").getJSONObject(0);

                weatherList.add(new Weather(dayObj.getLong("dt"),
                        tempObj.getDouble("min"),
                        tempObj.getDouble("max"),
                        dayObj.getDouble("humidity"),
                        weatherObj.getString("description"),
                        weatherObj.getString("icon")));

            }//end of for loop
        }catch (JSONException e){
            e.printStackTrace();
        }//end of try catchs
    }//end convertJSONtoArrayList

    public void getWeather(View view){
        EditText locationET = (EditText)findViewById(R.id.locationEditText);
        URL url = createURL(locationET.getText().toString());
        if(url != null ){
            dismissKeyboard(locationET);
            GetWeatherTask getLocalWeatherTask = new GetWeatherTask();
            getLocalWeatherTask.execute(url);
        }else{
            Toast.makeText(MainActivity.this,getString(R.string.invalid_url),Toast.LENGTH_SHORT).show();
        }
    }//end of getWeather

    private URL createURL(String city){
        String apiKey = getString(R.string.api_key),
                baseURL = getString(R.string.web_service_url);

        try{
            String urlString = baseURL
                    + URLEncoder.encode(city,"UTF-8")
                    + "&units=imperial&cnt=16&APPID="
                    + apiKey;

            return new URL(urlString);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    private  void dismissKeyboard(View v){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }
}//end MainActivity
