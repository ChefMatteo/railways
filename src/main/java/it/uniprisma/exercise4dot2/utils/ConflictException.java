package it.uniprisma.exercise4dot2.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@Slf4j
public class ConflictException extends RuntimeException {

    public ConflictException(String attr, String value) {
        super(String.format("Resource already exists with %s = %s", attr, value));
        log.info("Resource already exists with {} = {}", attr, value);
    }
}
