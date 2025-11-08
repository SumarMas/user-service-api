package com.platform.user_service.controllers;

import com.platform.user_service.dtos.request.NgoCreateRequestDto;
import com.platform.user_service.dtos.request.NgoUpdateRequestDto;
import com.platform.user_service.dtos.request.NgoValidationRequestDto;
import com.platform.user_service.dtos.common.NgoDto;
import com.platform.user_service.services.Ngo.INgoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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

    /**
     * Retrieves a list of NGOs pending approval.
     *
     * @return a ResponseEntity containing a list of pending NGOs
     * with HTTP status 200 (OK)
     */
    @GetMapping("/pending-approvals")
    public ResponseEntity<List<NgoDto>> getPendingNgoApprovals() {
        List<NgoDto> pendingNgos = ngoService.getPending();
        return ResponseEntity.ok(pendingNgos);
    }

    /**
     * Validates an NGO based on the provided ID and validation details.
     *
     * @param ngoId      the ID of the NGO to be validated
     * @param requestDto the validation details
     * @return a ResponseEntity with HTTP status 200 (OK)
     * if the validation is successful
     */
    @PutMapping("/{ngoId}/validate")
    public ResponseEntity<Void> validateNgo(@PathVariable UUID ngoId, @RequestBody NgoValidationRequestDto requestDto) {
        ngoService.validateNgo(ngoId, requestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates the details of an existing NGO.
     *
     * @param ngoId                the ID of the NGO to be updated
     * @param ngoUpdateRequestDto  the updated NGO details
     * @return a ResponseEntity with HTTP status 200 (OK)
     * if the update is successful
     */
    @PutMapping("/{ngoId}/update")
    public ResponseEntity<Void> updateNgo(@PathVariable UUID ngoId, @RequestBody @Valid NgoUpdateRequestDto ngoUpdateRequestDto) {
        ngoService.updateNgo(ngoId, ngoUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves a list of all approved NGOs.
     *
     * @return a ResponseEntity containing a list of approved NGOs
     * with HTTP status 200 (OK)
     */
    @GetMapping("/all-approved")
    public ResponseEntity<List<NgoDto>> getAllApprovedNgos() {
        List<NgoDto> approvedNgos = ngoService.getAllApproveNgos();
        return ResponseEntity.ok(approvedNgos);
    }
    /**
     * Retrieves the details of a specific NGO by its ID.
     *
     * @param ngoId the unique identifier of the NGO
     * @return a ResponseEntity containing the NgoDto representing the NGO details
     * with HTTP status 200 (OK)
     */
    @GetMapping("/{ngoId}")
    public ResponseEntity<NgoDto> getNgoById(@PathVariable UUID ngoId) {
        NgoDto ngoDto = ngoService.getNgoById(ngoId);
        return ResponseEntity.ok(ngoDto);
    }
    /**
     * Retrieves the NGO associated with the currently authenticated user.
     *
     * @return a ResponseEntity containing the NgoDto representing
     * the user's NGO details
     * with HTTP status 200 (OK)
     */
    @GetMapping("/my-ngo")
    public ResponseEntity<NgoDto> getMyNgo() {
        NgoDto ngoDto = ngoService.getMyNgo();
        return ResponseEntity.ok(ngoDto);
    }

    /**
     * Retrieves a list of all NGOs.
     *
     * @return a ResponseEntity containing a list of all NGOs
     * with HTTP status 200 (OK)
     */
    @GetMapping("/all")
    public ResponseEntity<List<NgoDto>> getAllNgos() {
        List<NgoDto> ngoDtos = ngoService.getAllNgos();
        return ResponseEntity.ok(ngoDtos);
    }
}
