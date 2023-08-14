package com.edts.concerts.controller;

import com.edts.concerts.dto.request.BookRequest;
import com.edts.concerts.dto.response.TicketResponse;
import com.edts.concerts.service.TicketService;
import com.edts.concerts.utils.helpers.ResponseApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/book")
    public ResponseApi<TicketResponse> bookTicket(@RequestBody BookRequest request) {
        String endpointInfo = "/book [POST]";
        String concertInfo = String.format("Concert ID: %s, Type: %s", request.getConcert_id(), request.getType());

        log.info("Endpoint {} - Booking ticket for concert: {}", endpointInfo, concertInfo);

        CompletableFuture<TicketResponse> bookedTicket = ticketService.bookTicket(request);

        log.info("Endpoint {} - Successfully booked ticket for concert: {}", endpointInfo, concertInfo);
        return ResponseApi.OK("success book concert", bookedTicket.join());
    }
}
