package com.example.android.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
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
    }
}
