package com.edts.concerts.service;

import com.edts.concerts.dto.request.BookRequest;
import com.edts.concerts.dto.response.TicketResponse;
import com.edts.concerts.entity.TicketEntity;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface TicketService {
    CompletableFuture<TicketResponse> bookTicket(BookRequest request);

    void validateTicketType(String ticketType);

    void updateTicketAvailability(TicketEntity ticketEntity);
}
