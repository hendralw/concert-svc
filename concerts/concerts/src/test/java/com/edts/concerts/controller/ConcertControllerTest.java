package com.edts.concerts.controller;

import com.edts.concerts.dto.response.ConcertResponse;
import com.edts.concerts.dto.response.Paging;
import com.edts.concerts.service.ConcertService;
import com.edts.concerts.utils.exceptions.CustomsException;
import com.edts.concerts.utils.helpers.CommonHelpers;
import com.edts.concerts.utils.helpers.ResponseApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ConcertControllerTest {
    @InjectMocks
    private ConcertController concertController;
    @Mock
    private ConcertService concertService;
    @Mock
    private CommonHelpers commonHelpers;

    @Test
    void testGetById_Success() {
        // Arrange
        UUID concertId = UUID.randomUUID();
        ConcertResponse concertResponse = new ConcertResponse();

        // Stub the behavior of the concertService.getById method
        when(concertService.getById(concertId)).thenReturn(concertResponse);

        // Act: Call the method under test
        ResponseApi<ConcertResponse> responseApi = concertController.getById(concertId);

        // Assert: Verify the interactions and check the response
        verify(concertService, times(1)).getById(concertId);

        assertEquals("success get data", responseApi.getMessage());
        assertEquals(concertResponse, responseApi.getData());
    }

    @Test
    void testGetById_ConcertNotFound() {
        // Arrange
        UUID concertId = UUID.randomUUID();

        // Stub the behavior of the concertService.getById method to throw an exception
        when(concertService.getById(concertId)).thenThrow(new CustomsException("Concert not found"));

        // Act and Assert: Verify that calling the controller method results in the expected exception
        assertThrows(CustomsException.class, () -> {
            concertController.getById(concertId);
        });

        // Verify that the concertService.getById method was called once with the correct argument
        verify(concertService, times(1)).getById(concertId);
    }

    @Test
    void testGetById_ConcertServiceError() {
        // Arrange
        UUID concertId = UUID.randomUUID();

        // Stub the behavior of the concertService.getById method to throw a service error exception
        when(concertService.getById(concertId)).thenThrow(new RuntimeException("Service error"));

        // Act and Assert: Verify that calling the controller method results in the expected service error exception
        assertThrows(RuntimeException.class, () -> {
            concertController.getById(concertId);
        });

        // Verify that the concertService.getById method was called once with the correct argument
        verify(concertService, times(1)).getById(concertId);
    }

    @Test
    void testGetConcerts_NoFilters() {
        // Arrange: Prepare a list of mock ConcertResponse objects
        List<ConcertResponse> mockConcerts = Collections.singletonList(new ConcertResponse());

        // Stub the behavior of concertService.getAllConcerts() to return a Paging containing mock concerts
        Paging<ConcertResponse> mockConcertPaging = new Paging<>(mockConcerts, 0, 10, mockConcerts.size());
        when(concertService.getAllConcertsWithPagination(anyInt(), anyInt())).thenReturn(mockConcertPaging);

        // Stub the behavior of commonHelpers.areAnyParamsNotNull() to return false for no filters
        when(commonHelpers.areAnyParamsNotNull(null, null)).thenReturn(false);

        // Act: Call the controller method with no filters
        ResponseApi<Paging<ConcertResponse>> response = concertController.getConcerts(null, null, 0, 10);

        // Assert: Verify that the response matches the expected values
        assertEquals("success get data", response.getMessage());
        assertEquals(mockConcertPaging, response.getData());

        // Verify that concertService.getAllConcerts() was called once and commonHelpers.areAnyParamsNotNull() was called once
        verify(concertService, times(1)).getAllConcertsWithPagination(anyInt(), anyInt());
        verify(commonHelpers, times(1)).areAnyParamsNotNull(null, null);
    }

    @Test
    void testGetConcerts_NoFilters_NoConcertsFound() {
        // Arrange: Prepare an empty list of concerts
        List<ConcertResponse> emptyConcerts = Collections.emptyList();

        // Create an empty Paging instance to represent no concerts found
        Paging<ConcertResponse> emptyPaging = new Paging<>(emptyConcerts, 0, 10, 0);

        // Stub the behavior of concertService.getAllConcerts() to return the empty Paging
        when(concertService.getAllConcertsWithPagination(anyInt(), anyInt())).thenReturn(emptyPaging);

        // Stub the behavior of commonHelpers.areAnyParamsNotNull() to return false for no filters
        when(commonHelpers.areAnyParamsNotNull(null, null)).thenReturn(false);

        // Act: Call the controller method with no filters
        ResponseApi<Paging<ConcertResponse>> response = concertController.getConcerts(null, null, 0, 10);

        // Assert: Verify that the response matches the expected values
        assertEquals("success get data", response.getMessage());
        assertEquals(emptyPaging, response.getData());

        // Verify that concertService.getAllConcerts() was called once and commonHelpers.areAnyParamsNotNull() was called once
        verify(concertService, times(1)).getAllConcertsWithPagination(anyInt(), anyInt());
        verify(commonHelpers, times(1)).areAnyParamsNotNull(null, null);
    }


    @Test
    void testGetConcerts_WithFilters() {
        // Arrange: Prepare a mock list of concerts with filters
        List<ConcertResponse> mockFilteredConcerts = Collections.singletonList(new ConcertResponse());

        // Create a Paging instance containing the mock filtered concerts
        Paging<ConcertResponse> mockFilteredPaging = new Paging<>(mockFilteredConcerts, 0, 10, mockFilteredConcerts.size());

        // Stub the behavior of concertService.searchConcertWithPagination() to return the mock filtered Paging
        when(concertService.searchConcertWithPagination("Concert A", "Venue X", 0, 10)).thenReturn(mockFilteredPaging);

        // Stub the behavior of commonHelpers.areAnyParamsNotNull() to return true for filters present
        when(commonHelpers.areAnyParamsNotNull("Concert A", "Venue X")).thenReturn(true);

        // Act: Call the controller method with filters
        ResponseApi<Paging<ConcertResponse>> response = concertController.getConcerts("Concert A", "Venue X", 0, 10);

        // Assert: Verify that the response matches the expected values
        assertEquals("success get data", response.getMessage());
        assertEquals(mockFilteredPaging, response.getData());

        // Verify that concertService.searchConcertWithPagination() was called once and commonHelpers.areAnyParamsNotNull() was called once
        verify(concertService, times(1)).searchConcertWithPagination("Concert A", "Venue X", 0, 10);
        verify(commonHelpers, times(1)).areAnyParamsNotNull("Concert A", "Venue X");
    }

    @Test
    void testGetConcerts_WithFilters_NoFilteredConcertsFound() {
        // Arrange: Stub the behavior of concertService.searchConcertWithPagination() to return an empty Paging
        Paging<ConcertResponse> emptyPaging = new Paging<>(Collections.emptyList(), 0, 10, 0);
        when(concertService.searchConcertWithPagination("Concert A", "Venue X", 0, 10)).thenReturn(emptyPaging);

        // Stub the behavior of commonHelpers.areAnyParamsNotNull() to return true for filters present
        when(commonHelpers.areAnyParamsNotNull("Concert A", "Venue X")).thenReturn(true);

        // Act: Call the controller method with filters
        ResponseApi<Paging<ConcertResponse>> response = concertController.getConcerts("Concert A", "Venue X", 0, 10);

        // Assert: Verify that the response matches the expected values
        assertEquals("success get data", response.getMessage());
        assertEquals(emptyPaging, response.getData());

        // Verify that concertService.searchConcertWithPagination() was called once and commonHelpers.areAnyParamsNotNull() was called once
        verify(concertService, times(1)).searchConcertWithPagination("Concert A", "Venue X", 0, 10);
        verify(commonHelpers, times(1)).areAnyParamsNotNull("Concert A", "Venue X");
    }

}
