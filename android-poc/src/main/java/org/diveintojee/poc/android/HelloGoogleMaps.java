package org.diveintojee.poc.android;

import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class HelloGoogleMaps extends MapActivity {

    Geocoder geocoder = null;

    MapView mapView = null;

    HelloGoogleMapsMakers markers = null;

    private void addMarkerToLocation(final GeoPoint location, final String label, final String description) {
        final OverlayItem overlayItem = new OverlayItem(location, label, description);
        markers.addOverlayItem(overlayItem);
        mapView.getOverlays().clear();
        mapView.getOverlays().add(markers);
    }

    private void animateToLocation(final GeoPoint location, final int zoom) {
        mapView.getController().setCenter(location);
        mapView.getController().animateTo(location);
        mapView.getController().setZoom(zoom);
    }

    @Override
    protected boolean isLocationDisplayed() {
        return false;
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapView = (MapView) findViewById(R.id.map);
        mapView.setBuiltInZoomControls(true);
        markers = new HelloGoogleMapsMakers(getResources().getDrawable(R.drawable.marker), mapView.getContext());
        // lat/long of Opéra Garnier, Paris

        final GeoPoint location = new GeoPoint((int) (48.871944 * 1E6), (int) (2.331667 * 1E6));
        addMarkerToLocation(location, "Opéra Garnier, 75009 Paris, France", "Some bla bla about Opéra Garnier");
        animateToLocation(location, 16);
        mapView.invalidate();

        final Button searchButton = (Button) findViewById(R.id.search);
        geocoder = new Geocoder(this);
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                final EditText loc = (EditText) findViewById(R.id.location);
                final String locationName = loc.getText().toString();
                List<Address> addressList = null;
                try {
                    addressList = geocoder.getFromLocationName(locationName, 1);
                } catch (final Throwable e) {
                    final String message = e.getMessage();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Log.e("android-poc", e.getMessage());
                }
                if (addressList == null || addressList.size() <= 0) {
                    final String message = "No address found for location [" + locationName + "]";
                    Log.i("android-poc", message);
                } else {
                    final Address address = addressList.get(0);
                    final String message = "Found address " + address.toString() + " for location [" + locationName
                        + "]";
                    Log.i("android-poc", message);
                    // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    final int lat = (int) (address.getLatitude() * 1E6);
                    final int lng = (int) (address.getLongitude() * 1E6);
                    final GeoPoint pt = new GeoPoint(lat, lng);
                    addMarkerToLocation(location, locationName, "Should find some stuff about this location");
                    animateToLocation(location, 16);
                    mapView.invalidate();
                }
            }
        });
    }
}
