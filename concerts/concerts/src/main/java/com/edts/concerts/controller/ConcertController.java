package com.edts.concerts.controller;

import com.edts.concerts.dto.response.ConcertResponse;
import com.edts.concerts.dto.response.Paging;
import com.edts.concerts.service.ConcertService;
import com.edts.concerts.utils.helpers.CommonHelpers;
import com.edts.concerts.utils.helpers.ResponseApi;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class ConcertController {

    private final ConcertService concertService;
    private CommonHelpers commonHelpers;

    @Operation(summary = "Get concert by id")
    @GetMapping("/concerts/{id}")
    public ResponseApi<ConcertResponse> getById(@PathVariable UUID id) {
        String endpointInfo = String.format("/concerts/%s [GET]", id);

        log.info("Endpoint {} - Fetching concert by ID: {}", endpointInfo, id);

        ConcertResponse concertResponse = concertService.getById(id);

        log.info("Endpoint {} - Successfully fetched concert with ID: {}", endpointInfo, id);
        return ResponseApi.OK("success get data", concertResponse);
    }

    @Operation(summary = "Get all concerts and search concerts with pagination")
    @GetMapping("/concerts")
    public ResponseApi<Paging<ConcertResponse>> getConcerts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String venue,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size) {

        String endpointInfo = "/concerts [GET]";

        log.info("Endpoint {} - Fetching concerts with filters - Name: {}, Venue: {}, Page: {}, Size: {}", endpointInfo, name, venue, page, size);

        Paging<ConcertResponse> concertPage;

        if (commonHelpers.areAnyParamsNotNull(name, venue)) {
            concertPage = concertService.searchConcertWithPagination(name, venue, page, size);
            log.info("Endpoint {} - Found {} concerts matching filters.", endpointInfo, concertPage.getTotalElements());
        } else {
            concertPage = concertService.getAllConcertsWithPagination(page, size);
            log.info("Endpoint {} - Fetched {} concerts.", endpointInfo, concertPage.getTotalElements());
        }

        return ResponseApi.OK("success get data", concertPage);
    }
}
