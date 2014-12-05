package com.dangchienhsgs.giffus.dialogs.mapdialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.map.GiftLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class LocationDialog extends DialogFragment
        implements GooglePlayServicesClient.OnConnectionFailedListener, GooglePlayServicesClient.ConnectionCallbacks, LocationListener {

    private GiftLocation location;
    private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;

    private MapFragment mapFragment;
    private TextView textView;

    private GoogleMap googleMap;
    private Marker myLocationMarker;

    private OnReturnLocationDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_map_location, null);


        textView = (TextView) view.findViewById(R.id.text_map_description);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);

        googleMap = mapFragment.getMap();


        if (location.isSecret()) {
            // if the location is secret
            if (!location.getHint().isEmpty()) {
                textView.setText("Hint: " + location.getHint());
            }

        } else {
            // if the location is visible
            textView.setText("Go to this: " + location.getMapTitle());
            LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().
                    position(pos).
                    title(location.getMapTitle());
            googleMap.addMarker(markerOptions);

            // Move camera to that position
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    pos).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

        builder.setView(view);

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().cancel();
                mLocationClient.disconnect();
            }
        });

        String positiveText;

        if (location.isSecret()) {
            positiveText = "I guess here";
        } else {
            positiveText = "I am in that location";
        }
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (Math.sqrt(Math.pow(location.getLatitude() - mCurrentLocation.getLatitude(), 2)
                        + Math.pow(location.getLongitude() - mCurrentLocation.getLongitude(), 2)) < 0.007) {
                    mListener.onReturnLocation(true);
                    Toast.makeText(getActivity(), "Confirm successful", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.onReturnLocation(false);
                    Toast.makeText(getActivity(), "You are fail !", Toast.LENGTH_SHORT).show();
                }

                Log.d("Location Dialog", location.getLatitude() + " " + mCurrentLocation.getLatitude() + " " + location.getLongitude() + mCurrentLocation.getLongitude());
            }
        });

        initLocation();

        return builder.create();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mapFragment != null) {
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
        }
    }

    public void initLocation() {
        mLocationClient = new LocationClient(getActivity(), this, this);

        // 4. create & set LocationRequest for Location update
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(1000 * 5);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(1000 * 1);

    }

    @Override
    public void onStart() {
        super.onStart();

        mLocationClient.connect();
    }

    public void setListener(OnReturnLocationDialogListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.disconnect();
    }

    public void setLocation(GiftLocation location) {
        this.location = location;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mLocationClient != null) {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
            if (mLocationClient != null) {
                // get location
                mCurrentLocation = mLocationClient.getLastLocation();
                MarkerOptions markerOptions = new MarkerOptions().
                        position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())).
                        title("Current Location");
                myLocationMarker = googleMap.addMarker(markerOptions);
                myLocationMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.location));
                if (location.isSecret()) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(
                            new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        }
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        myLocationMarker.remove();
        mCurrentLocation = mLocationClient.getLastLocation();
        MarkerOptions markerOptions = new MarkerOptions().
                position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())).
                title("Current Location");

        myLocationMarker = googleMap.addMarker(markerOptions);
        myLocationMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.location));
    }

    public interface OnReturnLocationDialogListener {
        public void onReturnLocation(boolean isChecked);
    }
}
