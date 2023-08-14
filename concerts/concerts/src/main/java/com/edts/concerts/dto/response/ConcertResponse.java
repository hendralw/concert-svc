package com.edts.concerts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConcertResponse {
    private UUID id;
    private String name;
    private Date date;
    private String venue;
    List<TicketResponse> tickets;

    @Data
    public static class TicketResponse {
        private UUID id;
        private String type;
        private float price;
        private int available_qty;
    }
}
