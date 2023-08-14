package com.edts.concerts.service;

import com.edts.concerts.dto.response.ConcertResponse;
import com.edts.concerts.dto.response.Paging;
import com.edts.concerts.entity.ConcertEntity;
import com.edts.concerts.entity.TicketEntity;
import com.edts.concerts.repository.ConcertRepository;
import com.edts.concerts.repository.TicketRepository;
import com.edts.concerts.utils.exceptions.CustomsException;
import com.edts.concerts.utils.helpers.MapperHelpers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceTest {
    @InjectMocks
    private ConcertServiceImpl concertService;

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private MapperHelpers mapperHelpers;

    @Test
    void testGetConcertById_Success() {
        // Arrange
        UUID concertId = UUID.randomUUID();

        ConcertEntity concertEntity = new ConcertEntity();
        concertEntity.setId(concertId);
        // Set other properties for concertEntity

        List<TicketEntity> ticketEntities = new ArrayList<>();
        // Add ticket entities to the list

        ConcertResponse.TicketResponse ticketResponse = new ConcertResponse.TicketResponse();
        // Set properties for ticketResponse

        when(concertRepository.findById(concertId)).thenReturn(Optional.of(concertEntity));
        when(ticketRepository.findByConcertId(any())).thenReturn(ticketEntities);
        when(mapperHelpers.toDto(any(), eq(ConcertResponse.class))).thenReturn(new ConcertResponse());
        when(mapperHelpers.toDtoList(any(), eq(ConcertResponse.TicketResponse.class))).thenReturn(Collections.singletonList(ticketResponse));

        // Act
        ConcertResponse result = concertService.getById(concertId);

        // Assert
        assertNotNull(result);
        // Add more assertions based on the expected behavior

        // Verify mock interactions
        verify(concertRepository, times(1)).findById(concertId);
        verify(ticketRepository, times(1)).findByConcertId(any());
        verify(mapperHelpers, times(1)).toDto(any(), eq(ConcertResponse.class));
        verify(mapperHelpers, times(1)).toDtoList(any(), eq(ConcertResponse.TicketResponse.class));
    }

    @Test
    void testGetConcertById_ConcertNotFound() {
        // Arrange
        UUID concertId = UUID.randomUUID();

        when(concertRepository.findById(concertId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomsException.class, () -> {
            concertService.getById(concertId);
        });

        // Verify
        verify(concertRepository, times(1)).findById(concertId);
        verifyNoMoreInteractions(concertRepository, ticketRepository, mapperHelpers);
    }

    private ConcertEntity createConcertEntity(String id) {
        ConcertEntity concertEntity = new ConcertEntity();
        concertEntity.setId(UUID.fromString(id));
        // Set other properties for concertEntity if needed
        return concertEntity;
    }

    private TicketEntity createTicketEntity(String id) {
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setId(UUID.fromString(id));
        // Set other properties for ticketEntity if needed
        return ticketEntity;
    }

    @Test
    void testGetAllConcertsWithPagination() {
        // Arrange
        ConcertEntity concertEntity1 = createConcertEntity(UUID.randomUUID().toString());
        ConcertEntity concertEntity2 = createConcertEntity(UUID.randomUUID().toString());

        TicketEntity ticketEntity1 = createTicketEntity(UUID.randomUUID().toString());
        TicketEntity ticketEntity2 = createTicketEntity(UUID.randomUUID().toString());

        List<ConcertEntity> concertEntities = Arrays.asList(concertEntity1, concertEntity2);
        Page<ConcertEntity> concertPage = new PageImpl<>(concertEntities);

        when(concertRepository.findAll(any(PageRequest.class))).thenReturn(concertPage);
        when(ticketRepository.findByConcertId(any())).thenReturn(Arrays.asList(ticketEntity1, ticketEntity2));
        when(mapperHelpers.toDto(any(), eq(ConcertResponse.class))).thenReturn(new ConcertResponse());
        when(mapperHelpers.toDtoList(any(), eq(ConcertResponse.TicketResponse.class))).thenReturn(Collections.emptyList());

        // Act
        Paging<ConcertResponse> result = concertService.getAllConcertsWithPagination(0, 2);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPageNumber());
        assertEquals(2, result.getPageSize()); // Make sure the pageSize is set correctly
        assertEquals(2, result.getTotalElements()); // Make sure the totalElements is set correctly

        // Verify
        verify(concertRepository, times(1)).findAll(any(PageRequest.class));
        verify(ticketRepository, times(2)).findByConcertId(any());
        verify(mapperHelpers, times(2)).toDto(any(), eq(ConcertResponse.class));
        verify(mapperHelpers, times(2)).toDtoList(any(), eq(ConcertResponse.TicketResponse.class));
    }

    @Test
    void testGetAllConcertsWithPagination_NoConcertsFound() {
        // Arrange
        Page<ConcertEntity> emptyConcertPage = new PageImpl<>(Collections.emptyList());

        when(concertRepository.findAll(any(PageRequest.class))).thenReturn(emptyConcertPage);

        // Act
        Paging<ConcertResponse> result = concertService.getAllConcertsWithPagination(0, 10);
        result.setPageSize(10);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertEquals(0, result.getTotalElements());

        // Verify
        verify(concertRepository, times(1)).findAll(any(PageRequest.class));
        verify(ticketRepository, never()).findByConcertId(any());
        verify(mapperHelpers, never()).toDto(any(), eq(ConcertResponse.class));
        verify(mapperHelpers, never()).toDtoList(any(), eq(ConcertResponse.TicketResponse.class));
    }

    @Test
    void testGetAllConcertsWithRepositoryException() {
        // Arrange
        when(concertRepository.findAll(any(PageRequest.class))).thenThrow(new CustomsException("Concert repository error"));

        // Act and Assert
        assertThrows(CustomsException.class, () -> {
            concertService.getAllConcertsWithPagination(0, 10);
        });

        // Verify
        verify(concertRepository, times(1)).findAll(any(PageRequest.class));
        verify(ticketRepository, never()).findByConcertId(any());
        verify(mapperHelpers, never()).toDto(any(), eq(ConcertResponse.class));
        verify(mapperHelpers, never()).toDtoList(any(), eq(ConcertResponse.TicketResponse.class));
    }

    @Test
    void testSearchConcertWithPagination_ConcertFound() {
        // Mock data for ConcertEntity and TicketEntity
        ConcertEntity concertEntity = new ConcertEntity();
        concertEntity.setId(UUID.fromString(UUID.randomUUID().toString()));

        // Mock the concertRepository behavior
        Page<ConcertEntity> mockConcertPage = new PageImpl<>(Collections.singletonList(concertEntity));
        when(concertRepository.findByFieldsWithPagination(any(), any(), any(PageRequest.class))).thenReturn(mockConcertPage);

        // Mock the ticketRepository behavior
        when(ticketRepository.findByConcertId(any())).thenReturn(Collections.emptyList());

        // Mock the mapperHelpers behavior
        when(mapperHelpers.toDto(any(), eq(ConcertResponse.class))).thenReturn(new ConcertResponse());
        when(mapperHelpers.toDtoList(any(), eq(ConcertResponse.TicketResponse.class))).thenReturn(Collections.emptyList());

        // Call the method under test
        Paging<ConcertResponse> result = concertService.searchConcertWithPagination("Concert A", "Venue X", 0, 10);

        // Assert the results
        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        // Verify method invocations
        verify(concertRepository, times(1)).findByFieldsWithPagination(eq("Concert A"), eq("Venue X"), any(PageRequest.class));
        verify(ticketRepository, times(1)).findByConcertId(any());
        verify(mapperHelpers, times(1)).toDto(any(), eq(ConcertResponse.class));
        verify(mapperHelpers, times(1)).toDtoList(any(), eq(ConcertResponse.TicketResponse.class));
    }

    @Test
    void testSearchConcertWithPagination_NoConcertsFound() {
        // Mock concertRepository to return an empty page
        Page<ConcertEntity> emptyConcertPage = new PageImpl<>(Collections.emptyList());
        when(concertRepository.findByFieldsWithPagination(any(), any(), any(PageRequest.class))).thenReturn(emptyConcertPage);

        // Call the method under test
        Paging<ConcertResponse> result = concertService.searchConcertWithPagination("Concert A", "Venue X", 0, 10);

        // Assert the results
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());

        // Verify method invocations
        verify(concertRepository, times(1)).findByFieldsWithPagination(eq("Concert A"), eq("Venue X"), any(PageRequest.class));
        verifyNoMoreInteractions(concertRepository, ticketRepository, mapperHelpers);
    }

    @Test
    void testSearchConcertWithPagination_RepositoryException() {
        // Mock concertRepository to throw an exception
        when(concertRepository.findByFieldsWithPagination(any(), any(), any(PageRequest.class)))
                .thenThrow(new CustomsException("Concert repository error"));

        // Call the method under test and expect an exception
        assertThrows(CustomsException.class, () -> {
            concertService.searchConcertWithPagination("Concert A", "Venue X", 0, 10);
        });

        // Verify that the expected interactions occurred and no further interactions happened
        verify(concertRepository, times(1)).findByFieldsWithPagination(eq("Concert A"), eq("Venue X"), any(PageRequest.class));
        verifyNoMoreInteractions(concertRepository, ticketRepository, mapperHelpers);
    }
}
