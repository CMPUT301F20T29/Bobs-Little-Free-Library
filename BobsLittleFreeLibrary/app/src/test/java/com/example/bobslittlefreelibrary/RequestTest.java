package com.example.bobslittlefreelibrary;

import com.example.bobslittlefreelibrary.models.Request;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RequestTest {

    private Request mockRequest() {
        return new Request(mockRequester(), mockProvider(), mockBookID(), null, mockBookTitle());
    }

    private String mockRequester() {
        return "9EC0qH6AHAXaZjg0roAmLv1i7ap1";
    }

    private String mockProvider() {
        return "lTu4tdKwMVZr7abqakjZ5QU1PIF3";
    }

    private String mockBookID() {
        return "Fl3VTwjQV1g90XhKsNsw";
    }

    private String mockBookTitle() {
        return "Behavior Modification: Principles and Procedures";
    }

    private double mockLat() {
        return -34;
    }

    private double mockLong() {
        return 151;
    }

    @Test
    public void testSetLocation() {
        Request testRequest = mockRequest();

        assertEquals(testRequest.getLatitude(), 1000.0);
        assertEquals(testRequest.getLongitude(), 1000.0);

        testRequest.setLatitude(mockLat());
        testRequest.setLongitude(mockLong());
        assertEquals(testRequest.getLatitude(), mockLat());
        assertEquals(testRequest.getLongitude(), mockLong());
    }

    @Test
    public void testSetReturnRequest() {
        Request testRequest = mockRequest();

        assertFalse(testRequest.isReturnRequest());
        testRequest.changeToReturnRequest();
        assertTrue(testRequest.isReturnRequest());
        testRequest.setReturnRequest(false);
        assertFalse(testRequest.isReturnRequest());
    }
}
