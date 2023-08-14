package com.edts.concerts.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcertRequest {
    private UUID id;

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "date can't be null")
    @FutureOrPresent(message = "date must be in the present or future")
    private Date date;

    @NotBlank(message = "venue is required")
    private String venue;
}
