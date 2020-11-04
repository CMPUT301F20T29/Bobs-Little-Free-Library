package com.example.bobslittlefreelibrary;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RequestTest {

    private Requests mockRequest() {
        return new Requests(mockRequester(), mockProvider(), mockBookID(), null, mockBookTitle());
    }

    private String mockRequester() {
        return "bob123";
    }

    private String mockProvider() {
        return "jessie123";
    }

    private String mockBookID() {
        return "Fl3VTwjQV1g90XhKsNsw";
    }

    private String mockBookTitle() {
        return "Behavior Modification: Principles and Procedures";
    }

    @Test
    public void testGetSetReqStatus() {
        Requests testRequest = mockRequest();
        assertEquals("sent", testRequest.getReqStatus());

        testRequest.setReqStatus("pendingExchange");
        assertEquals("pendingExchange", testRequest.getReqStatus());
    }

    //TODO: Test for location

}
