package com.example.labo3.controllers;

import com.example.labo3.dto.request.CreateSpecimenRequest;
import com.example.labo3.dto.request.UpdateSpecimenRequest;
import com.example.labo3.dto.response.GeneralResponse;
import com.example.labo3.dto.response.PageableResponse;
import com.example.labo3.dto.response.SpecimenResponse;
import com.example.labo3.services.SpecimenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/specimens")
@RequiredArgsConstructor
public class SpecimenController {

    private final SpecimenService specimenService;

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse<SpecimenResponse>> createSpecimen(
            @Valid @RequestBody CreateSpecimenRequest request,
            HttpServletRequest servletRequest
    ) {
        SpecimenResponse response = specimenService.createSpecimen(request);

        return buildResponse(
                "Specimen registered successfully in Sheikah Slate",
                HttpStatus.CREATED,
                response,
                servletRequest
        );
    }

    @GetMapping("/list")
    public ResponseEntity<GeneralResponse<PageableResponse<SpecimenResponse>>> getAllSpecimens(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            HttpServletRequest servletRequest
    ) {
        PageableResponse<SpecimenResponse> response = specimenService.getAllSpecimens(
                page,
                size,
                sortBy,
                sortOrder
        );

        return buildResponse(
                "Specimens retrieved successfully from Sheikah Slate",
                HttpStatus.OK,
                response,
                servletRequest
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse<SpecimenResponse>> getSpecimenById(
            @PathVariable UUID id,
            HttpServletRequest servletRequest
    ) {
        SpecimenResponse response = specimenService.getSpecimenById(id);

        return buildResponse(
                "Specimen found successfully in Sheikah Slate records",
                HttpStatus.OK,
                response,
                servletRequest
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GeneralResponse<SpecimenResponse>> updateSpecimen(
            @PathVariable UUID id,
            @RequestBody UpdateSpecimenRequest request,
            HttpServletRequest servletRequest
    ) {
        SpecimenResponse response = specimenService.updateSpecimen(id, request);

        return buildResponse(
                "Specimen updated successfully in Sheikah Slate",
                HttpStatus.OK,
                response,
                servletRequest
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GeneralResponse<SpecimenResponse>> deleteSpecimen(
            @PathVariable UUID id,
            HttpServletRequest servletRequest
    ) {
        SpecimenResponse response = specimenService.deleteSpecimen(id);

        return buildResponse(
                "Specimen deleted successfully from Sheikah Slate",
                HttpStatus.OK,
                response,
                servletRequest
        );
    }

    private <T> ResponseEntity<GeneralResponse<T>> buildResponse(
            String message,
            HttpStatus status,
            T data,
            HttpServletRequest request
    ) {
        GeneralResponse<T> response = GeneralResponse.<T>builder()
                .message(message)
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .data(data)
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
