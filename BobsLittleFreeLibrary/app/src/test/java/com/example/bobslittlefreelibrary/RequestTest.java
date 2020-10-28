package com.example.bobslittlefreelibrary;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RequestTest {

    private Requests mockRequest() {
        return new Requests(mockRequester(), mockProvider(), mockBook());
    }

    private User mockRequester() {
        return new User("bob123", "asdf", "bob123@gmail.com",
                "5th Street");
    }

    private User mockProvider() {
        return new User("jessie123", "qwerty", "jessie123@gmail.com",
                "10th Street");
    }

    private Book mockBook() {
        return new Book("The Cat in the Hat", "available",
                "The Cat in the Hat is a 1957 children's book written and illustrated by " +
                        "Theodor Geisel under the pen name Dr. Seuss. The story centers on a tall anthropomorphic" +
                        " cat who wears a red and white-striped hat and a red bow tie. " +
                        "The Cat shows up at the house of Sally and her brother one rainy day when " +
                        "their mother is away. Despite the repeated objections of the children's fish," +
                        " the Cat shows the children a few of his tricks in an attempt to entertain them." +
                        " In the process, he and his companions, Thing One and Thing Two, wreck the house." +
                        " The children and the fish become more and more alarmed until the Cat" +
                        " produces a machine that he uses to clean everything up and disappears just" +
                        " before the children's mother comes home.",
                "Dr. Seuss", mockProvider(), "9780007158447");
    }

    @Test
    public void testGetSetReqStatus() {
        Requests testRequest = mockRequest();
        assertEquals("sent", testRequest.getReqStatus());

        testRequest.setReqStatus("pendingExchange");
        assertEquals("pendingExchange", testRequest.getReqStatus());
    }

    //TODO: Test for location

    @Test
    public void testGetStaticReqInfo() {
        Requests testRequest = mockRequest();

        assertEquals(testRequest.getBookRequested().getTitle(), mockBook().getTitle());
        assertEquals(testRequest.getReqReceiver().getUsername(), mockProvider().getUsername());
        assertEquals(testRequest.getReqSender().getUsername(), mockRequester().getUsername());
    }
}
