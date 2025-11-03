package com.platform.user_service.controllers;

import com.platform.user_service.dtos.common.UserDto;
import com.platform.user_service.dtos.request.UserRegisterDto;
import com.platform.user_service.dtos.request.UserUpdateDto;
import com.platform.user_service.dtos.response.TokenResponseDto;
import com.platform.user_service.dtos.response.UserLoginResponseDto;
import com.platform.user_service.services.user.IUserService;
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

import java.util.UUID;

/**
 * Controller for handling user-related requests.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    /**
     * Service for handling user-related operations.
     */
    private final IUserService userService;

    /**
     * Handles user registration requests.
     *
     * @param user the user registration details
     * @return a ResponseEntity containing the TokenResponseDto
     * with the generated JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> registerUser(@RequestBody @Valid UserRegisterDto user) {
        return ResponseEntity.ok(userService.crateUser(user));
    }

    /**
     * Handles user data login.
     * @param userId id user to get data
     * @return a ResponseEntity containing the UserLoginResponseDto
     * whit id and roles user.
     */
    @GetMapping("{userId}/datalogin")
    public ResponseEntity<UserLoginResponseDto> getDataLogin(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.getDataLogin(userId));
    }

    /**
     * Handles user update requests.
     *
     * @param userId the ID of the user to update
     * @param userUpdateDto the user update details
     * @return a ResponseEntity with HTTP status 200 (OK) if the update is successful
     */
    @PutMapping("/{userId}/update")
    public ResponseEntity<Void> updateUser(@PathVariable("userId")UUID userId, @RequestBody @Valid UserUpdateDto userUpdateDto) {
        userService.updateUser(userId, userUpdateDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves the profile information of the currently authenticated user.
     *
     * @return a ResponseEntity containing the UserDto
     * with the user's profile details
     */
    @GetMapping("/my-profile")
    public ResponseEntity<UserDto> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }
}
