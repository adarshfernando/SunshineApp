package com.example.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarshf on 12/17/15.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // allows fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            new FetchWeatherTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // creates an ArrayList of raw forecast data
        List<String> weekForecast = new ArrayList<>();
        weekForecast.add("Today - Sunny - 88/63");
        weekForecast.add("Tomorrow - Foggy - 70/46");
        weekForecast.add("Wed - Cloudy - 72/63");
        weekForecast.add("Thu - Rainy - 64/51");
        weekForecast.add("Fri - Foggy - 70/46");
        weekForecast.add("Sat - Sunny - 76/68");

        // converts raw forecast data to list TextViews
        ArrayAdapter<String> listArrayAdapter =
                new ArrayAdapter<>(
                        // The current context
                        getActivity(),
                        // ID of the list item layout
                        R.layout.list_item_forecast,
                        // ID of target textView to populate
                        R.id.list_item_forecast_textview,
                        // forecast data
                        weekForecast);
        // creates a ListView object and populate it with forecast_listview
        // This is needed because you need to make a java object from the xml element,
        // if you want to refer to it in a .java class.
        // findViewById is a Activity and View method, that's why we use the rootView
        // object (defined above) to get to it
        ListView forecastListView =
                (ListView) rootView.findViewById(
                        R.id.forecast_listview);
        // binds the ListView object to the ArrayAdapter created earlier
        forecastListView.setAdapter(listArrayAdapter);

        return rootView;
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        /* BOILER PLATE CODE SNIPPET from UDACITY*/

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        //API Key


        protected String doInBackground(String... weatherData) {
            try

            {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                // Variables for custom url
                String forecastPeriod = "daily";
                String forecastZip = "98005";
                String forecastUnits = "metric";
                String forecastCount = "7";
                final String apiKey = "16f7461b02124fab04d85c3c3953c00b";

                // Builds URL String for custom user inputs; initialized with default params
                Uri.Builder urlBuilder = new Uri.Builder();
                urlBuilder.scheme("http")
                        .authority("api.openweathermap.org")
                        .appendPath("data")
                        .appendPath("2.5")
                        .appendPath("forecast")
                        .appendPath(forecastPeriod)
                        .appendQueryParameter("q", forecastZip)
                        .appendQueryParameter("mode", "json")
                        .appendQueryParameter("units", forecastUnits)
                        .appendQueryParameter("cnt", forecastCount)
                        .appendQueryParameter("APPID", apiKey);
                //constructs URL from uri.builder
                URL url = new URL(urlBuilder.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Forecast JSON String: " + forecastJsonStr);
            } catch (
                    IOException e
                    )

            {
                Log.e("ForecastFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally

            {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("ForecastFragment", "Error closing stream", e);
                    }
                }
            }
            return forecastJsonStr;
        }
    }
}

