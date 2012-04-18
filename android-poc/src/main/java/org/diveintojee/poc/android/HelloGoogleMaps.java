package org.diveintojee.poc.android;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
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
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class HelloGoogleMaps extends MapActivity {

    public class GeoUpdateHandler implements LocationListener {

        public void onLocationChanged(final Location location) {
            final int lat = (int) (location.getLatitude() * 1E6);
            final int lng = (int) (location.getLongitude() * 1E6);
            final GeoPoint point = new GeoPoint(lat, lng);
            createMarker();
            mapController.animateTo(point);
            mapController.setCenter(point);
        }

        public void onProviderDisabled(final String provider) {}

        public void onProviderEnabled(final String provider) {}

        public void onStatusChanged(final String provider, final int status, final Bundle extras) {}

    }

    private MapController mapController;
    private MapView mapView;
    private LocationManager locationManager;
    private MyOverlays itemizedoverlay;

    private MyLocationOverlay myLocationOverlay;

    private void createMarker() {
        final GeoPoint p = mapView.getMapCenter();
        final OverlayItem overlayitem = new OverlayItem(p, "", "");
        itemizedoverlay.addOverlay(overlayitem);
        if (itemizedoverlay.size() > 0) {
            mapView.getOverlays().add(itemizedoverlay);
        }
    }

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
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);
        mapController = mapView.getController();
        myLocationOverlay = new MyLocationOverlay(this, mapView);
        mapView.getOverlays().add(myLocationOverlay);

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        final String provider = locationManager.getBestProvider(criteria, true);
        locationManager.getLastKnownLocation(provider);

        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapView.getController().animateTo(myLocationOverlay.getMyLocation());
            }
        });

        final Drawable drawable = getResources().getDrawable(R.id.pin);
        itemizedoverlay = new MyOverlays(this, drawable);
        createMarker();

        final EditText editText = (EditText) findViewById(R.id.location);
        editText.setOnKeyListener(new OnKeyListener() {
            /**
             * @param v
             * @param keyCode
             * @param event
             * @return
             */
            public boolean onKey(final View v, final int keyCode, final KeyEvent event) {
                if (keyCode != KeyEvent.KEYCODE_ENTER)
                    return false;
                final String location = editText.getText().toString();
                updateLocation(location);
                return false;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onResume();
        myLocationOverlay.disableMyLocation();
        myLocationOverlay.disableCompass();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableCompass();
    }

    private final void updateLocation(final String locationAsString) {
        final double geoLat = location.getLatitude() * 1E6;
        final double geoLng = location.getLongitude() * 1E6;
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
        listOfOverlays.add(myLocationOverlay);

        mapView.invalidate();

    }
}
