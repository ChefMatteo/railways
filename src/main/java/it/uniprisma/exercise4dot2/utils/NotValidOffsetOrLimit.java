package it.uniprisma.exercise4dot2.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Slf4j
public class NotValidOffsetOrLimit extends RuntimeException {
    public NotValidOffsetOrLimit(String offsetOrLimit, Integer n) {
        super(String.format("%s %s not valid", offsetOrLimit, n));
        log.info("{} {} not valid", offsetOrLimit, n);
    }
}
