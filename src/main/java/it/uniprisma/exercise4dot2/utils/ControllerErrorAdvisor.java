package it.uniprisma.exercise4dot2.utils;

import com.google.gson.Gson;
import it.uniprisma.exercise4dot2.Exercise4dot2Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerErrorAdvisor {
    Gson gson = Exercise4dot2Application.gson();

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex){
        return ResponseEntity.status(BadRequestException.class.getAnnotation(ResponseStatus.class).value()).body(gson.toJson(ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(ConflictException ex){
        return ResponseEntity.status(ConflictException.class.getAnnotation(ResponseStatus.class).value()).body(gson.toJson("ConflictException occurred: " +ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException ex){
        return ResponseEntity.status(NotFoundException.class.getAnnotation(ResponseStatus.class).value()).body(gson.toJson("NotFoundException occurred: " + ex.getMessage()));
    }


}
