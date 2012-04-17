package org.diveintojee.poc.android.test;

import android.test.ActivityInstrumentationTestCase2;
import org.diveintojee.poc.android.*;

public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<HelloGoogleMaps> {

    public HelloAndroidActivityTest() {
        super(HelloGoogleMaps.class); 
    }

    public void testActivity() {
        HelloGoogleMaps activity = getActivity();
        assertNotNull(activity);
    }
}

