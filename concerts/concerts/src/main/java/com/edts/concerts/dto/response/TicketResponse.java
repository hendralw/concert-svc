package com.edts.concerts.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class TicketResponse {
    private UUID id;
    private String concert_id;
    private String type;
    private float price;
    private int available_qty;
}
