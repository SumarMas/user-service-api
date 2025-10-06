package com.platform.user_service.controllers;

import com.platform.user_service.dtos.request.NgoCreateRequestDto;
import com.platform.user_service.services.Ngo.INgoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling NGO-related requests.
 */
@RestController
@RequestMapping("/api/v1/ngos")
@RequiredArgsConstructor
public class NgoController {
    /**
     * Service for handling NGO-related operations.
     */
    private final INgoService ngoService;

    /**
     * Handles NGO registration requests.
     *
     * @param ngoCreateRequestDto the NGO creation details
     * @return a ResponseEntity with HTTP status 200 (OK)
     * if the registration is successful
     */
    @PostMapping("/register")
    public ResponseEntity<Void> registerNgo(@RequestBody @Valid NgoCreateRequestDto ngoCreateRequestDto) {
        ngoService.registerNgo(ngoCreateRequestDto);
        return ResponseEntity.ok().build();
    }
}
