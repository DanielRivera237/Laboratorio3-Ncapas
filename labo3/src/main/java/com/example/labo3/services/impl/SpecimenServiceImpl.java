package com.example.labo3.services.impl;

import com.example.labo3.dto.request.CreateSpecimenRequest;
import com.example.labo3.dto.request.UpdateSpecimenRequest;
import com.example.labo3.dto.response.PageableResponse;
import com.example.labo3.dto.response.SpecimenResponse;
import com.example.labo3.entities.Specimen;
import com.example.labo3.exceptions.ResourceNotFoundException;
import com.example.labo3.mappers.SpecimenMapper;
import com.example.labo3.repositories.SpecimenRepository;
import com.example.labo3.services.SpecimenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecimenServiceImpl implements SpecimenService {

    private final SpecimenRepository specimenRepository;
    private final SpecimenMapper specimenMapper;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id",
            "name",
            "region",
            "dangerLevel",
            "isFriendly"
    );

    @Override
    @Transactional
    public SpecimenResponse createSpecimen(CreateSpecimenRequest request) {
        Specimen specimen = specimenMapper.toEntityCreate(request);
        Specimen savedSpecimen = specimenRepository.save(specimen);

        return specimenMapper.toDto(savedSpecimen);
    }

    @Override
    public PageableResponse<SpecimenResponse> getAllSpecimens(
            int page,
            int size,
            String sortBy,
            String sortOrder
    ) {
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );

        Page<Specimen> specimenPage = specimenRepository.findAll(pageable);

        if (specimenPage.isEmpty()) {
            throw new ResourceNotFoundException("No specimens are registered in Hyrule");
        }

        Page<SpecimenResponse> dtoPage = specimenMapper.toDtoPage(specimenPage);

        return PageableResponse.<SpecimenResponse>builder()
                .content(dtoPage.getContent())
                .page(dtoPage.getNumber())
                .size(dtoPage.getSize())
                .totalElements(dtoPage.getTotalElements())
                .totalPages(dtoPage.getTotalPages())
                .last(dtoPage.isLast())
                .sortBy(sortBy)
                .sortOrder(direction.name())
                .build();
    }

    @Override
    public SpecimenResponse getSpecimenById(UUID id) {
        Specimen specimen = specimenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Specimen not found in Sheikah Slate records"
                ));

        return specimenMapper.toDto(specimen);
    }

    @Override
    @Transactional
    public SpecimenResponse updateSpecimen(UUID id, UpdateSpecimenRequest request) {
        this.getSpecimenById(id);

        Specimen specimenToUpdate = specimenMapper.toEntityUpdate(request, id);
        Specimen updatedSpecimen = specimenRepository.save(specimenToUpdate);

        return specimenMapper.toDto(updatedSpecimen);
    }

    @Override
    @Transactional
    public SpecimenResponse deleteSpecimen(UUID id) {
        SpecimenResponse existingSpecimen = this.getSpecimenById(id);

        specimenRepository.deleteById(id);

        return existingSpecimen;
    }
}