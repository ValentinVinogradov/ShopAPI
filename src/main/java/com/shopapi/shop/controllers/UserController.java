package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.JWTTokenResponseDTO;
import com.shopapi.shop.dto.UserFieldsRequestDTO;
import com.shopapi.shop.dto.UserResponseDTO;
import com.shopapi.shop.enums.UUIDTokenType;
import com.shopapi.shop.impl.UserServiceImpl;
import com.shopapi.shop.models.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/users/v1")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    //todo подумать что возвращать и как

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable UUID userId){
        try {
            return ResponseEntity.ok(userService.getById(userId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/username")
    public ResponseEntity<UserResponseDTO> getUserByUsername(Principal principal) {
        try {
            User user = userService.getUserByUsername(principal.getName());
            return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/change-username")
    public ResponseEntity<String> changeUsername(@RequestBody UserFieldsRequestDTO usernameDTO, Principal principal) {
        try {
            userService.changeUsername(principal.getName(), usernameDTO.newUsername());
            return ResponseEntity.ok("Username changed for user with name " + principal.getName());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("User with this username already exists");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to change username");
        }
    }

    //todo подумать над тем надо ли будет разлогинить пользователя после смены пароля (да если несколько сеансов)

    @PostMapping("/generate-verify-token")
    public ResponseEntity<String> generateVerifyToken(@RequestBody UserFieldsRequestDTO verifyDTO) {
        try {
             return ResponseEntity.ok(userService.generateToken(verifyDTO.email(), UUIDTokenType.EMAIL));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to generate verify token");
        }
    }

    @PostMapping("/check-verify-token")
    public ResponseEntity<String> checkEmailToken(@RequestBody UserFieldsRequestDTO verifyDTO) {
        try {
            return ResponseEntity.ok(userService.checkToken(verifyDTO.token(), UUIDTokenType.EMAIL));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/check-signup-token")
    public ResponseEntity<JWTTokenResponseDTO> checkSignUpToken(@RequestBody UserFieldsRequestDTO verifyDTO) {
        try {
            return ResponseEntity.ok(userService.checkSignUpToken(verifyDTO.token()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/change-email")
    public ResponseEntity<String> changeEmail(@RequestBody UserFieldsRequestDTO emailDTO, Principal principal) {
        try {
            userService.changeEmail(principal.getName(), emailDTO.newEmail());
            return ResponseEntity.ok(userService.generateToken(emailDTO.newEmail(), UUIDTokenType.EMAIL));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to change email");
        }
    }

    @PostMapping("add-phone-number")
    public ResponseEntity<String> addPhoneNumber() {
        return null;
    }
}
