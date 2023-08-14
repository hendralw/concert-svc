package com.edts.concerts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paging <T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
}
