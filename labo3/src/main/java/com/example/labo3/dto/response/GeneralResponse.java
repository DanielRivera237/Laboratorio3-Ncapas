package com.example.labo3.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse<T> {

    private String message;
    private Integer status;
    private LocalDateTime timestamp;
    private String path;
    private T data;
}