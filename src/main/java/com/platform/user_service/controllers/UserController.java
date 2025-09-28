package com.platform.user_service.controllers;

import com.platform.user_service.dtos.request.UserRegisterDto;
import com.platform.user_service.dtos.response.TokenResponseDto;
import com.platform.user_service.services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling user-related requests.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    /** Service for handling user-related operations. */
    private final IUserService userService;

    /**
     * Handles user registration requests.
     *
     * @param user the user registration details
     * @return a ResponseEntity containing the TokenResponseDto with the generated JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> registerUser(@RequestBody @Valid UserRegisterDto user) {
        return ResponseEntity.ok(userService.crateUser(user));
    }
}
