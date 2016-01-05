package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void onOpenLocationSelected() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String userZip = sharedPref.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));

        String uriBase = "geo:0,0?";
        Uri userZipUri = Uri.parse(uriBase).buildUpon()
                .appendQueryParameter("q", userZip)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(userZipUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(LOG_TAG, "Couldn't call " + userZip + ", no receiving apps installed!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsActivityIntent = new Intent(
                    this,
                    SettingsActivity.class);
            startActivity(settingsActivityIntent);
            return true;
        }


        if (id == R.id.action_map) {
            onOpenLocationSelected();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
