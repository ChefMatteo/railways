package it.uniprisma.exercise4dot2.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String id) {
        super(String.format("Resource not found with id: %s", id));
        log.error(String.format("Resource not found with id: %s", id));

    }
    public NotFoundException() {
        super("Resource not found");
        log.error("Resource not found");
    }
}
