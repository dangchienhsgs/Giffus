package com.dangchienhsgs.giffus.map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.utils.PreferencesHandler;
import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.utils.URLContentHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class AddLocationActivity extends Activity implements AdapterView.OnItemClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, AddLocationDialog.AddLocationListener {

    private String TAG = "Add Location Activity";

    private AutoCompleteTextView searchBox;

    private PlacesAutoCompleteAdapter mAdapter;

    private GoogleMap googleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        searchBox = (AutoCompleteTextView) findViewById(R.id.edit_text_auto_complete);

        mAdapter = new PlacesAutoCompleteAdapter(
                this,
                android.R.layout.simple_list_item_1
        );

        searchBox.setAdapter(mAdapter);
        searchBox.setOnItemClickListener(this);

        try {
            initizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment))
                    .getMap();

            googleMap.setOnInfoWindowClickListener(this);
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(final Marker marker) {
                    View view = getLayoutInflater().inflate(R.layout.layout_info_window_google_map, null);
                    TextView placeTitle = (TextView) view.findViewById(R.id.location_title);
                    placeTitle.setText(marker.getTitle());
                    return view;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });

            if (googleMap == null) {
                Toast.makeText(this, "Unnable to create Map", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String title = mAdapter.getItem(i);
        String ref = mAdapter.getReference(i);
        new DownloadPlaceInformation(title, ref).execute();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        GiftLocation location = new GiftLocation();
        location.setMapTitle(marker.getTitle());
        location.setGoogleMapID(marker.getId());
        location.setLatitude(marker.getPosition().latitude);
        location.setLongitude(marker.getPosition().longitude);

        AddLocationDialog addLocationDialog = new AddLocationDialog();
        addLocationDialog.setLocation(location);

        addLocationDialog.show(getFragmentManager(), "Set location properties");

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            return false;
        } else {
            marker.showInfoWindow();
            return true;
        }
    }

    @Override
    public void onLocationAdded(GiftLocation location) {
        // Do something

        Toast.makeText(getApplicationContext(), "Location added", Toast.LENGTH_SHORT).show();

        // Save data to location

        // Convert location to json string
        Gson gson = new Gson();
        String jsonLocation = gson.toJson(location, GiftLocation.class);

        // Create intent and set result
        Intent intent = new Intent();
        intent.putExtra(Common.JSON_GIFFUS_LOCATION, jsonLocation);

        PreferencesHandler.saveValueToPreferences(Common.JSON_GIFFUS_LOCATION, jsonLocation, this);

        Log.d(TAG, jsonLocation);

        setResult(RESULT_OK, intent);
        AddLocationActivity.this.finish();
    }

    private class DownloadPlaceInformation extends AsyncTask<String, Void, LatLng> {
        private String description;
        private String refs;

        private DownloadPlaceInformation(String description, String refs) {
            this.description = description;
            this.refs = refs;
        }

        @Override
        protected LatLng doInBackground(String... strings) {
            String url = MapServer.getPlaceDetailUrl(refs);
            Log.d(TAG, url);
            String jsonResults = URLContentHandler.getURLContent(url);
            Log.d(TAG, jsonResults);
            try {
                JSONObject places = new JSONObject(jsonResults);
                JSONObject result = places.getJSONObject("result");
                JSONObject geometry = result.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");

                Log.d(TAG, location.toString());

                LatLng latLng = new LatLng(
                        Double.parseDouble(location.getString("lat")),
                        Double.parseDouble(location.getString("lng"))
                );

                return latLng;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "JSON Exception");
                return null;
            }
        }

        @Override
        protected void onPostExecute(LatLng result) {
            // Add a maker to the google map

            MarkerOptions markerOptions = new MarkerOptions().position(result).title(description);
            googleMap.addMarker(markerOptions);

            // Move camera to that position
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    result).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

}
