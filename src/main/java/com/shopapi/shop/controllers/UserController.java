package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.UserFieldsRequestDTO;
import com.shopapi.shop.enums.UUIDTokenType;
import com.shopapi.shop.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users/v1")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/username")
    public ResponseEntity<String> getUsername(Principal principal){
        System.out.println("зашли в контроллер для username");
        return ResponseEntity.ok(principal.getName());
    }

    //todo подумать над тем чтобы хранилось UUID пользователя вместо имени в токене

    @PostMapping("/change-username")
    public ResponseEntity<String> changeUsername(@RequestBody UserFieldsRequestDTO usernameDTO, Principal principal) {
        try {
            userService.changeUsername(principal.getName(), usernameDTO.getNewUsername());
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
             return ResponseEntity.ok(userService.generateToken(verifyDTO.getEmail(), UUIDTokenType.EMAIL));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to generate verify token");
        }
    }

    @PostMapping("/check-verify-token")
    public ResponseEntity<String> checkEmailToken(@RequestBody UserFieldsRequestDTO verifyDTO) {
        try {
            return ResponseEntity.ok(userService.checkToken(verifyDTO.getToken(), UUIDTokenType.EMAIL));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/change-email")
    public ResponseEntity<String> changeEmail(@RequestBody UserFieldsRequestDTO emailDTO, Principal principal) {
        try {
            userService.changeEmail(principal.getName(), emailDTO.getNewEmail());
            return ResponseEntity.ok(userService.generateToken(emailDTO.getNewEmail(), UUIDTokenType.EMAIL));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to change email");
        }
    }

    @PostMapping("add-phone-number")
    public ResponseEntity<String> addPhoneNumber() {
        return null;
    }
}
