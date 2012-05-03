package org.diveintojee.poc.android;

import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import de.akquinet.android.androlog.Log;

public class HelloGoogleMaps extends MapActivity implements OnClickListener, OnKeyListener {

    private Geocoder geocoder;

    private MapView mapView;

    private HelloGoogleMapsMakers markers;

    private GeoPoint lastLocation;

    private EditText locationEditText;

    /**
     * @param location
     * @param label
     * @param description
     */
    private void addMarkerToLocation(final GeoPoint location, final String label, final String description) {
        final OverlayItem overlayItem = new OverlayItem(location, label, description);
        markers.addOverlayItem(overlayItem);
        mapView.getOverlays().clear();
        mapView.getOverlays().add(markers);
    }

    /**
     * @param location
     * @param zoom
     */
    private void animateToLocation(final GeoPoint location, final int zoom) {
        mapView.getController().setCenter(location);
        mapView.getController().animateTo(location);
        mapView.getController().setZoom(zoom);
    }

    /**
     * 
     */
    protected void doSearch() {

        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
            locationEditText.getWindowToken(), 0);

        final String locationName = locationEditText.getText().toString();
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
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            Log.i("android-poc", message);
        } else {
            final Address address = addressList.get(0);
            final String message = "Found address " + address.toString() + " for location [" + locationName + "]";
            Log.i("android-poc", message);
            // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            final int lat = (int) (address.getLatitude() * 1E6);
            final int lng = (int) (address.getLongitude() * 1E6);
            final GeoPoint foundLocation = new GeoPoint(lat, lng);
            final String locationDescription = "Should find some stuff about this location";
            handleNewLocation(foundLocation, locationName, locationDescription);
        }

    }

    /**
     * @param foundLocation
     * @param locationName
     * @param locationDescription
     */
    private void handleNewLocation(final GeoPoint foundLocation, final String locationName,
            final String locationDescription) {
        mapView.invalidate();
        animateToLocation(foundLocation, 16);
        addMarkerToLocation(foundLocation, locationName, locationDescription);
        lastLocation = foundLocation;
    }

    private void initializeView() {
        // lat/long of Opéra Garnier, Paris
        final GeoPoint defaultLocation = new GeoPoint((int) (48.871944 * 1E6), (int) (2.331667 * 1E6));
        final String locationName = "Opéra Garnier, 75009 Paris, France";
        final String locationDescription = "Some bla bla about Opéra Garnier";
        handleNewLocation(defaultLocation, locationName, locationDescription);
    }

    @Override
    protected boolean isLocationDisplayed() {
        return false;
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private void listenToClicks() {
        final Button searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(this);
        final Button zoomInButton = (Button) findViewById(R.id.zoomin);
        zoomInButton.setOnClickListener(this);
        final Button zoomOutButton = (Button) findViewById(R.id.zoomout);
        zoomOutButton.setOnClickListener(this);
        final Button centerMapButton = (Button) findViewById(R.id.center_map);
        centerMapButton.setOnClickListener(this);
    }

    /**
     * 
     */
    private void onCenterMapButtonClick() {
        animateToLocation(lastLocation, 16);
    }

    /**
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(final View view) {

        switch (view.getId()) {
            case R.id.search:
                onSearchButtonClick();
                break;
            case R.id.zoomin:
                onZoomInButtonClick();
                break;
            case R.id.zoomout:
                onZoomOutButtonClick();
                break;
            case R.id.center_map:
                onCenterMapButtonClick();
                break;
        }

    }

    /**
     * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        geocoder = new Geocoder(this);
        locationEditText = (EditText) findViewById(R.id.location);
        locationEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mapView = (MapView) findViewById(R.id.map);
        mapView.setBuiltInZoomControls(false);
        markers = new HelloGoogleMapsMakers(getResources().getDrawable(R.drawable.marker), mapView.getContext());
        initializeView();
        listenToClicks();
    }

    /**
     * @see android.view.View.OnKeyListener#onKey(android.view.View, int, android.view.KeyEvent)
     */

    @Override
    public boolean onKey(final View v, final int keyCode, final KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            doSearch();
        }
        return false;
    }

    /**
     * 
     */
    private void onSearchButtonClick() {
        doSearch();
    }

    /**
     * 
     */
    private void onZoomInButtonClick() {
        mapView.getController().zoomIn();
    }

    /**
     * 
     */
    private void onZoomOutButtonClick() {
        mapView.getController().zoomOut();
    }

}
