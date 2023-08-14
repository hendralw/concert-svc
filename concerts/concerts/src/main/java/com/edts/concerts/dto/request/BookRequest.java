package com.edts.concerts.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    @NotBlank(message = "concert_id is required")
    private String concert_id;

    @NotBlank(message = "type is required")
    private String type;
}
