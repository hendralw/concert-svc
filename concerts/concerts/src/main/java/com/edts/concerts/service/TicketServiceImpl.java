package com.edts.concerts.service;

import com.edts.concerts.dto.request.BookRequest;
import com.edts.concerts.dto.response.TicketResponse;
import com.edts.concerts.entity.TicketEntity;
import com.edts.concerts.enums.TicketType;
import com.edts.concerts.repository.TicketRepository;
import com.edts.concerts.utils.exceptions.CustomsException;
import com.edts.concerts.utils.helpers.CommonHelpers;
import com.edts.concerts.utils.helpers.MapperHelpers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {


    private final TicketRepository ticketRepository;
    private final ConcertService concertService;
    private final MapperHelpers mapperHelpers;
    private CommonHelpers commonHelpers;

    private final Object updateLock = new Object(); // Create a lock object for synchronization

    @Override
    @Transactional
    @Async
    public CompletableFuture<TicketResponse> bookTicket(BookRequest request) {
        log.info("Booking ticket for concert with ID: {}, Type: {}", request.getConcert_id(), request.getType());

        commonHelpers.validateRequest(request);
        concertService.getConcertById(UUID.fromString(request.getConcert_id()));
        validateTicketType(request.getType());

        TicketEntity ticket = ticketRepository.findTicketByConcertIdAndType(request.getConcert_id(), request.getType())
                .orElseThrow(() -> new CustomsException("no available ticket found"));

        synchronized (updateLock) {
            updateTicketAvailability(ticket); // Synchronize access to the update method
        }

        log.info("Successfully booked ticket for concert with ID: {}, Type: {}", request.getConcert_id(), request.getType());
        return CompletableFuture.completedFuture(mapperHelpers.toDto(ticket, TicketResponse.class));
    }

    @Override
    public void validateTicketType(String ticketType) throws CustomsException {
        log.info("Validating ticket type: {}", ticketType);
        TicketType.valueOf(ticketType.toUpperCase());
        log.info("Ticket type validation successful for: {}", ticketType);
    }

    @Override
    public void updateTicketAvailability(TicketEntity ticket) {
        log.info("Updating ticket availability for ticket with ID: {}", ticket.getId());

        synchronized (updateLock) {
            int updatedQty = ticket.getAvailable_qty() - 1;
            if (updatedQty < 0) {
                throw new CustomsException("No more available tickets");
            }

            try {
                ticket.setAvailable_qty(updatedQty);
                ticketRepository.save(ticket);
                log.info("Successfully updated ticket availability for ticket with ID: {}", ticket.getId());
            } catch (Exception e) {
                log.error("Error updating ticket availability for ticket with ID: {}", ticket.getId(), e);
                throw new CustomsException("Error updating ticket availability", e);
            }
        }
    }
}
