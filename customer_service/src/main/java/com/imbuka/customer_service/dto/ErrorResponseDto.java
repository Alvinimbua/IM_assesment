package com.imbuka.customer_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(
        name = "Error Response",
        description = "Schema to hold error response information"
)
public class ErrorResponseDto {
    @Schema(
            description = "API pathway invoked by client"
    )
    private String apiPath;

    @Schema(
            description = "Error code representing the error code that happened"
    )
    private HttpStatus errorCode;
    @Schema(
            description = "Error message representing the error code that happened"
    )
    private String errorMessage;
    @Schema(
            description = "Time representing the error happened"
    )
    private LocalDateTime errorTime;
}
