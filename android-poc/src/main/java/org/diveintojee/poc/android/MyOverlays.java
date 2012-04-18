/*
 *
 */
package org.diveintojee.poc.android;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * @author louis.gueye@gmail.com
 */
public class MyOverlays extends ItemizedOverlay<OverlayItem> {

    private final class CancelOnClickListener implements DialogInterface.OnClickListener {
        public void onClick(final DialogInterface dialog, final int which) {
            Toast.makeText(context, "You clicked yes", Toast.LENGTH_LONG).show();
        }
    }

    private final class OkOnClickListener implements DialogInterface.OnClickListener {
        public void onClick(final DialogInterface dialog, final int which) {
            Toast.makeText(context, "You clicked no", Toast.LENGTH_LONG).show();
        }
    }

    private static int maxNum = 5;
    private final OverlayItem overlays[] = new OverlayItem[maxNum];
    private int index = 0;
    private boolean full = false;

    private final Context context;

    private OverlayItem previousoverlay;

    public MyOverlays(final Context context, final Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
        this.context = context;
    }

    public void addOverlay(final OverlayItem overlay) {
        if (previousoverlay != null) {
            if (index < maxNum) {
                overlays[index] = previousoverlay;
            } else {
                index = 0;
                full = true;
                overlays[index] = previousoverlay;
            }
            index++;
            populate();
        }
        previousoverlay = overlay;
    }

    @Override
    protected OverlayItem createItem(final int i) {
        return overlays[i];
    };

    @Override
    protected boolean onTap(final int index) {
        final Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("This will end the activity");
        builder.setCancelable(true);
        builder.setPositiveButton("I agree", new OkOnClickListener());
        builder.setNegativeButton("No, no", new CancelOnClickListener());
        final AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    @Override
    public int size() {
        if (full)
            return overlays.length;
        else
            return index;

    }
}
