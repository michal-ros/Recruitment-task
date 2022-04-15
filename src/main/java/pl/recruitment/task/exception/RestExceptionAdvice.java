package pl.recruitment.task.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class RestExceptionAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    ResponseErrorDetails handleObjectNotFoundException(ObjectNotFoundException ex) {
        return ResponseErrorDetails.builder()
                .message(HttpStatus.NOT_FOUND.getReasonPhrase())
                .status(HttpStatus.NOT_FOUND.value())
                .description(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    ResponseErrorDetails handleIOException(IOException ex) {
        return ResponseErrorDetails.builder()
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .description(ex.getMessage())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseErrorDetails handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseErrorDetails.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .description(ex.getMessage())
                .build();
    }

    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseErrorDetails handleJsonProcessingException(JsonProcessingException ex) {
        return ResponseErrorDetails.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .description(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
    }
}
