package com.edts.concerts.service;

import com.edts.concerts.dto.response.ConcertResponse;
import com.edts.concerts.dto.response.Paging;
import com.edts.concerts.entity.ConcertEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ConcertService {
    ConcertResponse getById(UUID id);

    ConcertEntity getConcertById(UUID id);

    Paging<ConcertResponse> getAllConcertsWithPagination(int pageNumber, int pageSize);

    Paging<ConcertResponse> searchConcertWithPagination(String name, String venue, int pageNumber, int pageSize);
}
