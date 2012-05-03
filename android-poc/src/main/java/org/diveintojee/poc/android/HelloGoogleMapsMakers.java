/*
 *
 */
package org.diveintojee.poc.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * @author louis.gueye@gmail.com
 */
public class HelloGoogleMapsMakers extends ItemizedOverlay<OverlayItem> {

    private final List<OverlayItem> overlayItems = new ArrayList<OverlayItem>();
    private final Context context;
    private Paint innerPaint, borderPaint;

    /**
     * @param defaultMarker
     */
    public HelloGoogleMapsMakers(final Drawable defaultMarker, final Context context) {
        super(boundCenterBottom(defaultMarker));
        this.context = context;
    }

    /**
     * @param overlayItem
     */
    public void addOverlayItem(final OverlayItem overlayItem) {
        overlayItems.add(overlayItem);
        populate();
    }

    /**
     * @see com.google.android.maps.ItemizedOverlay#createItem(int)
     */
    @Override
    protected OverlayItem createItem(final int itemIndex) {
        return overlayItems.get(itemIndex);
    }

    // @Override
    // public void draw(final Canvas canvas, final MapView mapView, final boolean shadow) {
    // final Projection projection = mapView.getProjection();
    // for (final OverlayItem overlayItem : overlayItems) {
    // final GeoPoint myLocationGeoPoint = overlayItem.getPoint();
    // final Point myPoint = new Point();
    // projection.toPixels(myLocationGeoPoint, myPoint);
    // final int radiusPixel = (int) projection.metersToEquatorPixels(50);
    // canvas.drawCircle(myPoint.x, myPoint.y, radiusPixel, getInnerPaint());
    // canvas.drawCircle(myPoint.x, myPoint.y, radiusPixel, getBorderPaint());
    // }
    // super.draw(canvas, mapView, shadow);
    //
    // }

    public Paint getBorderPaint() {
        if (borderPaint == null) {
            borderPaint = new Paint();
            borderPaint.setARGB(255, 68, 89, 82);
            borderPaint.setAntiAlias(true);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(2);
        }
        return borderPaint;
    }

    public Paint getInnerPaint() {
        if (innerPaint == null) {
            innerPaint = new Paint();
            innerPaint.setARGB(225, 68, 89, 82); // gray
            innerPaint.setAntiAlias(true);
        }
        return innerPaint;
    }

    /**
     * @see com.google.android.maps.ItemizedOverlay#onTap(int)
     */
    @Override
    protected boolean onTap(final int index) {
        final OverlayItem item = overlayItems.get(index);
        final StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("** " + item.getTitle() + " **");
        messageBuilder.append("\n");
        messageBuilder.append(item.getSnippet());
        Toast.makeText(context, messageBuilder.toString(), Toast.LENGTH_LONG).show();
        return true;
    }

    /**
     * @see com.google.android.maps.ItemizedOverlay#size()
     */

    @Override
    public int size() {
        return overlayItems.size();
    }

}
