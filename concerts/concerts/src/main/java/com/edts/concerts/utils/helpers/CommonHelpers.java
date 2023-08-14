package com.edts.concerts.utils.helpers;

import com.edts.concerts.dto.response.Paging;
import com.edts.concerts.utils.exceptions.CustomsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class CommonHelpers {

    private final Validator validator;

    public <T> void validateRequest(T data) {
        Set<ConstraintViolation<T>> violations = validator.validate(data);

        if (!violations.isEmpty()) {
            List<String> errMsgList = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());

            String errMsg = String.join(", ", errMsgList);
            log.error("Validation error(s): {}", errMsg);
            throw new CustomsException("Validation error(s): " + errMsg);
        }
    }

    public boolean areAnyParamsNotNull(Object... params) {
        for (Object param : params) {
            if (param != null) {
                return true;
            }
        }
        return false;
    }

    public static <T> Paging<T> createPaging(List<T> content, Page<?> page) {
        return new Paging<>(content, page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
