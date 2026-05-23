package com.example.labo3.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    private String message;
    private Integer status;
    private String error;
    private LocalDateTime timestamp;
    private String path;
}
