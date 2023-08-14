package com.edts.concerts.service;

import com.edts.concerts.dto.request.BookRequest;
import com.edts.concerts.dto.response.TicketResponse;
import com.edts.concerts.entity.ConcertEntity;
import com.edts.concerts.entity.TicketEntity;
import com.edts.concerts.enums.TicketType;
import com.edts.concerts.repository.TicketRepository;
import com.edts.concerts.utils.exceptions.CustomsException;
import com.edts.concerts.utils.helpers.CommonHelpers;
import com.edts.concerts.utils.helpers.MapperHelpers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Mock
    private CommonHelpers commonHelpers;

    @Mock
    private ConcertService concertService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private MapperHelpers mapperHelpers;

    private final Object updateLock = new Object();

    @Test
    void testBookTicket_Success() throws InterruptedException, ExecutionException, TimeoutException {
        // Create a mock TicketEntity
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setId(UUID.randomUUID());
        ticketEntity.setAvailable_qty(1);

        // Mock the commonHelpers.validateRequest method
        doNothing().when(commonHelpers).validateRequest(any(BookRequest.class));

        // Mock the concertService.getConcertById method
        when(concertService.getConcertById(any(UUID.class))).thenReturn(new ConcertEntity());

        // Mock the ticketRepository.findTicketByConcertIdAndType method
        when(ticketRepository.findTicketByConcertIdAndType(anyString(), anyString())).thenReturn(Optional.of(ticketEntity));

        // Mock the ticketRepository.save method
        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(ticketEntity);

        // Create a mock TicketResponse
        TicketResponse ticketResponse = new TicketResponse();
        when(mapperHelpers.toDto(any(), eq(TicketResponse.class))).thenReturn(ticketResponse);

        // Create a mock BookRequest
        BookRequest bookRequest = new BookRequest();
        bookRequest.setConcert_id(UUID.randomUUID().toString());
        bookRequest.setType(String.valueOf(TicketType.VIP));

        // Call the method under test asynchronously
        CompletableFuture<TicketResponse> future = ticketService.bookTicket(bookRequest);

        // Wait for the CompletableFuture to complete
        TicketResponse result = future.get(5, TimeUnit.SECONDS);

        assertNotNull(result);
        assertEquals(ticketResponse, result);

        // Verify that the relevant methods were called
        verify(commonHelpers, times(1)).validateRequest(any(BookRequest.class));
        verify(concertService, times(1)).getConcertById(any(UUID.class));
        verify(ticketRepository, times(1)).findTicketByConcertIdAndType(anyString(), anyString());
        verify(ticketRepository, times(1)).save(any(TicketEntity.class));
        verify(mapperHelpers, times(1)).toDto(any(), eq(TicketResponse.class));
    }

    @Test
    void testBookTicket_NoAvailableTicketFound() {
        // Prepare a BookRequest
        BookRequest request = new BookRequest();
        String concertId = UUID.randomUUID().toString();
        request.setConcert_id(concertId);
        request.setType(String.valueOf(TicketType.VIP));  // Assuming a valid type here

        // Mock the behavior of findTicketByConcertIdAndType to return an empty optional
        when(ticketRepository.findTicketByConcertIdAndType(any(), any()))
                .thenReturn(Optional.empty());

        // Call the bookTicket method and expect a CustomsException to be thrown
        assertThrows(CustomsException.class, () -> {
            ticketService.bookTicket(request);
        });

        // Verify that findTicketByConcertIdAndType was called with the correct arguments
        verify(ticketRepository, times(1))
                .findTicketByConcertIdAndType(eq(concertId), eq(String.valueOf(TicketType.VIP)));
    }

    @Test
    void testBookTicket_InvalidTicketType() {
        // Prepare a BookRequest with an invalid ticket type
        BookRequest request = new BookRequest();
        request.setConcert_id(UUID.randomUUID().toString());
        request.setType("INVALID_TYPE");

        // Call the bookTicket method and expect an exception
        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.bookTicket(request);
        });
    }

    @Test
    void testBookTicket_InvalidGuidConcertId() {
        // Prepare a BookRequest with an invalid ticket type
        BookRequest request = new BookRequest();
        request.setConcert_id("your-id");
        request.setType(String.valueOf(TicketType.VIP));

        // Call the bookTicket method and expect an exception
        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.bookTicket(request);
        });
    }

    @Test
    void testUpdateTicketAvailability_Success() {
        // Create a mock TicketEntity
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setId(UUID.randomUUID());
        ticketEntity.setAvailable_qty(5); // Assuming there are 5 tickets available initially

        // Mock the ticketRepository.save method
        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(ticketEntity);

        // Call the method under test
        ticketService.updateTicketAvailability(ticketEntity);

        // Verify that the save method was called and ticket availability was updated
        verify(ticketRepository, times(1)).save(any(TicketEntity.class));
        assertEquals(4, ticketEntity.getAvailable_qty()); // Check that availability was decremented by 1
    }

    @Test
    void testUpdateTicketAvailability_Error() {
        // Create a mock TicketEntity
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setId(UUID.randomUUID());
        ticketEntity.setAvailable_qty(1); // Available tickets

        // Mock the ticketRepository.save method to throw an exception
        doThrow(new RuntimeException("Database error")).when(ticketRepository).save(any(TicketEntity.class));

        // Simulate synchronization using the same lock object
        synchronized (updateLock) {
            // Call the method under test and expect a CustomsException
            assertThrows(CustomsException.class, () -> {
                ticketService.updateTicketAvailability(ticketEntity);
            });
        }

        // Verify that the save method was called
        verify(ticketRepository, times(1)).save(any(TicketEntity.class));
    }

    @Test
    void testValidateTicketType_ValidType() {
        // Act and assert
        assertDoesNotThrow(() -> {
            ticketService.validateTicketType("VIP");
        });
    }

    @Test
    void testValidateTicketType_InvalidType() {
        // Act and assert
        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.validateTicketType("INVALID_TYPE");
        });
    }
}
