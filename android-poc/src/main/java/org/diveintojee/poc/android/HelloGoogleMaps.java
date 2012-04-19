package org.diveintojee.poc.android;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class HelloGoogleMaps extends MapActivity {

    private MapController mapController;
    private MapView mapView;
    private LocationManager locationManager;
    private EditText editText;

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main); // bind the layout to the activity

        // Configure the Map
        mapView = (MapView) findViewById(R.id.map);
        editText = (EditText) findViewById(R.id.location);

        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(false);
        mapController = mapView.getController();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        if (locationManager != null) {
            final String provider = locationManager.getBestProvider(criteria, true);
            final Location initialLocation = locationManager.getLastKnownLocation(provider);
            updateLocation(initialLocation.getLatitude(), initialLocation.getLongitude());
        }

        editText.setOnKeyListener(new OnKeyListener() {
            /**
             * @param v
             * @param keyCode
             * @param event
             * @return
             */
            @Override
            public boolean onKey(final View v, final int keyCode, final KeyEvent event) {
                final String locationAsString = editText.getText().toString();
                if (keyCode != KeyEvent.KEYCODE_ENTER && locationAsString.length() < 3)
                    return false;

                final Geocoder geo = new Geocoder(HelloGoogleMaps.this.getApplicationContext(), Locale.getDefault());
                List<Address> addresses = Collections.emptyList();
                try {
                    addresses = geo.getFromLocationName(locationAsString, 1);
                } catch (final IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (addresses.isEmpty()) {
                    editText.setText("Waiting for Location");
                } else {
                    if (addresses.size() > 0) {
                        final Address firstAddress = addresses.get(0);
                        editText.setText(firstAddress.getFeatureName() + ", " + firstAddress.getLocality() + ", "
                            + firstAddress.getAdminArea() + ", " + firstAddress.getCountryName());
                        // Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() +
                        // addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                        updateLocation(firstAddress.getLatitude(), firstAddress.getLongitude());
                    }
                }
                return false;
            }
        });

    }

    private final void updateLocation(final double latitude, final double longitude) {
        final double geoLat = latitude * 1E6;
        final double geoLng = longitude * 1E6;
        // converting to integer values which can be passed into the geo point.
        final int mylat = (int) geoLat;
        final int mylng = (int) geoLng;

        final GeoPoint point = new GeoPoint(mylat, mylng);
        mapController.animateTo(point);
        mapController.setZoom(16);
        mapController.setCenter(point);

        // ---Add a location marker---
        final List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        // listOfOverlays.add(new Overlay() {});
        mapView.invalidate();

    }
}
