package pl.recruitment.task.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseErrorDetails {
    private String message;
    private int status;
    private String description;
}