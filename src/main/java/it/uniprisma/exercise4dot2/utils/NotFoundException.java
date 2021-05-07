package it.uniprisma.exercise4dot2.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String id) {
        super(String.format("Resource not found with id: %s", id));
    }
    public NotFoundException() {
        super(String.format("Resource not found"));
    }
}
