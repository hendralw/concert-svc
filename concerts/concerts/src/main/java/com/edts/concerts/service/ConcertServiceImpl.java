package com.edts.concerts.service;

import com.edts.concerts.dto.response.ConcertResponse;
import com.edts.concerts.dto.response.Paging;
import com.edts.concerts.entity.ConcertEntity;
import com.edts.concerts.entity.TicketEntity;
import com.edts.concerts.repository.TicketRepository;
import com.edts.concerts.repository.ConcertRepository;
import com.edts.concerts.utils.exceptions.CustomsException;
import com.edts.concerts.utils.helpers.CommonHelpers;
import com.edts.concerts.utils.helpers.MapperHelpers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.edts.concerts.utils.helpers.CommonHelpers.createPaging;

@Service
@AllArgsConstructor
@Slf4j
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepository;
    private final TicketRepository ticketRepository;
    private final MapperHelpers mapperHelpers;

    @Override
    public ConcertResponse getById(UUID id){
        log.info("Fetching concert by ID: {}", id);

        ConcertEntity concert = getConcertById(id);

        List<TicketEntity> tickets = ticketRepository.findByConcertId(concert.getId().toString());

        ConcertResponse concertResponse = mapperHelpers.toDto(concert, ConcertResponse.class);
        List<ConcertResponse.TicketResponse> ticketResponses = mapperHelpers.toDtoList(tickets, ConcertResponse.TicketResponse.class);
        concertResponse.setTickets(ticketResponses);

        log.info("Successfully fetched concert with ID: {}", id);
        return concertResponse;
    }

    @Override
    public ConcertEntity getConcertById(UUID id) {
        return concertRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Concert not found for ID: {}", id);
                    return new CustomsException("concert not found");
                });
    }

//    @Override
//    public List<ConcertResponse> getAllConcerts() {
//        log.info("Fetching all concerts");
//
//        List<ConcertEntity> concerts = concertRepository.findAll();
//
//        List<ConcertResponse> concertResponses = concerts.stream()
//                .map(concert -> {
//                    List<TicketEntity> tickets = ticketRepository.findByConcertId(concert.getId().toString());
//                    List<ConcertResponse.TicketResponse> ticketResponses = mapperHelpers.toDtoList(tickets, ConcertResponse.TicketResponse.class);
//
//                    ConcertResponse concertResponse = mapperHelpers.toDto(concert, ConcertResponse.class);
//                    concertResponse.setTickets(ticketResponses);
//
//                    return concertResponse;
//                })
//                .collect(Collectors.toList());
//
//        log.info("Fetched {} concerts", concertResponses.size());
//        return concertResponses;
//    }

    @Override
    public Paging<ConcertResponse> getAllConcertsWithPagination(int pageNumber, int pageSize) {
        log.info("Fetching concerts with pagination - Page: {}, PageSize: {}", pageNumber, pageSize);

        Page<ConcertEntity> concertPage = concertRepository.findAll(PageRequest.of(pageNumber, pageSize));

        List<ConcertResponse> concertResponses = concertPage.getContent().stream()
                .map(concert -> {
                    List<TicketEntity> tickets = ticketRepository.findByConcertId(concert.getId().toString());
                    List<ConcertResponse.TicketResponse> ticketResponses = mapperHelpers.toDtoList(tickets, ConcertResponse.TicketResponse.class);

                    ConcertResponse concertResponse = mapperHelpers.toDto(concert, ConcertResponse.class);
                    concertResponse.setTickets(ticketResponses);

                    return concertResponse;
                })
                .collect(Collectors.toList());

        log.info("Fetched {} concerts on page {} out of {} total pages", concertResponses.size(), concertPage.getNumber(), concertPage.getTotalPages());

        return createPaging(concertResponses, concertPage);
    }

//    @Override
//    public List<ConcertResponse> searchConcert(String name, String venue) {
//        log.info("Searching concerts with filters - Name: {}, Venue: {}", name, venue);
//
//        List<ConcertEntity> concerts = concertRepository.findByFields(name, venue);
//
//        List<ConcertResponse> concertResponses = concerts.stream()
//                .map(concert -> {
//                    List<TicketEntity> tickets = ticketRepository.findByConcertId(concert.getId().toString());
//                    List<ConcertResponse.TicketResponse> ticketResponses = mapperHelpers.toDtoList(tickets, ConcertResponse.TicketResponse.class);
//
//                    ConcertResponse concertResponse = mapperHelpers.toDto(concert, ConcertResponse.class);
//                    concertResponse.setTickets(ticketResponses);
//
//                    return concertResponse;
//                })
//                .collect(Collectors.toList());
//
//        log.info("Found {} concerts matching filters.", concertResponses.size());
//        return concertResponses;
//    }

    @Override
    public Paging<ConcertResponse> searchConcertWithPagination(String name, String venue, int pageNumber, int pageSize) {
        log.info("Searching concerts with filters and pagination - Name: {}, Venue: {}, Page: {}, PageSize: {}", name, venue, pageNumber, pageSize);

        Page<ConcertEntity> concertPage = concertRepository.findByFieldsWithPagination(name, venue, PageRequest.of(pageNumber, pageSize));

        List<ConcertResponse> concertResponses = concertPage.getContent().stream()
                .map(this::mapConcertEntityToResponse)
                .collect(Collectors.toList());

        log.info("Found {} concerts matching filters on page {} out of {} total pages", concertResponses.size(), concertPage.getNumber(), concertPage.getTotalPages());

        return createPaging(concertResponses, concertPage);
    }

    private ConcertResponse mapConcertEntityToResponse(ConcertEntity concert) {
        List<TicketEntity> tickets = ticketRepository.findByConcertId(concert.getId().toString());
        List<ConcertResponse.TicketResponse> ticketResponses = mapperHelpers.toDtoList(tickets, ConcertResponse.TicketResponse.class);

        ConcertResponse concertResponse = mapperHelpers.toDto(concert, ConcertResponse.class);
        concertResponse.setTickets(ticketResponses);

        return concertResponse;
    }
}
