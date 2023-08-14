package com.edts.concerts.controller;

import com.edts.concerts.dto.request.BookRequest;
import com.edts.concerts.dto.response.TicketResponse;
import com.edts.concerts.enums.TicketType;
import com.edts.concerts.service.TicketService;
import com.edts.concerts.utils.helpers.ResponseApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {

    @InjectMocks
    private TicketController ticketController;
    @Mock
    private TicketService ticketService;

    @Test
    void testBookTicket_SuccessfulBooking() {
        // Arrange: Create a valid booking request
        BookRequest request = new BookRequest();
        request.setConcert_id(UUID.randomUUID().toString());
        request.setType(String.valueOf(TicketType.VIP));

        // Create a dummy ticket response
        TicketResponse ticketResponse = new TicketResponse();

        // Mock the behavior of ticketService.bookTicket() to return the dummy response
        when(ticketService.bookTicket(request)).thenReturn(CompletableFuture.completedFuture(ticketResponse));

        // Act: Call the bookTicket method on the controller
        ResponseApi<TicketResponse> responseApi = ticketController.bookTicket(request);

        // Assert: Check if the response matches the expected values
        assertEquals("success book concert", responseApi.getMessage());
        assertEquals(ticketResponse, responseApi.getData());

        // Verify: Ensure that ticketService.bookTicket() was called with the correct request
        verify(ticketService).bookTicket(request);
    }

    @Test
    void testBookTicket_BookingError() {
        // Arrange: Create a booking request
        BookRequest request = new BookRequest();
        request.setConcert_id(UUID.randomUUID().toString());
        request.setType("VIP");

        // Mock ticketService.bookTicket() to throw an exception when called
        when(ticketService.bookTicket(request)).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Booking error")));

        // Act and Assert: Call the bookTicket method and expect an exception
        assertThrows(CompletionException.class, () -> {
            ticketController.bookTicket(request);
        });

        // Verify: Ensure that ticketService.bookTicket() was called with the correct request
        verify(ticketService).bookTicket(request);
    }

    @Test
    void testBookTicket_InvalidRequest() {
        // Arrange: Create an invalid booking request (missing concert_id and type)
        BookRequest request = new BookRequest();

        // Mock the behavior of ticketService.bookTicket() to throw an exception
        when(ticketService.bookTicket(request)).thenThrow(new CompletionException(new IllegalArgumentException("Invalid request")));

        // Act and Assert: Call the bookTicket method and expect an exception
        assertThrows(CompletionException.class, () -> {
            ticketController.bookTicket(request);
        });

        // Verify: Ensure that ticketService.bookTicket() was called with the correct request
        verify(ticketService).bookTicket(request);
    }

    @Test
    void testBookTicket_BookingException() {
        // Arrange: Create a valid booking request
        BookRequest request = new BookRequest();
        request.setConcert_id(UUID.randomUUID().toString());
        request.setType(String.valueOf(TicketType.VIP));

        // Mock the behavior of ticketService.bookTicket() to throw an exception
        when(ticketService.bookTicket(request)).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Booking error")));

        // Act and Assert: Call the bookTicket method and expect an exception
        assertThrows(RuntimeException.class, () -> {
            ticketController.bookTicket(request);
        });

        // Verify: Ensure that ticketService.bookTicket() was called with the correct request
        verify(ticketService).bookTicket(request);
    }
}