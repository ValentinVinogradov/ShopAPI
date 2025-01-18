package com.shopapi.shop.controllers;

import com.shopapi.shop.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users/v1")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/username")
    public ResponseEntity<String> getUsername(Principal principal){
        System.out.println("зашли в контроллер для username");
        return ResponseEntity.status(HttpStatus.OK).body(principal.getName());
    }

    @PostMapping("/change-username")
    public ResponseEntity<String> changeUsername(@RequestBody String newUsername, Principal principal) {
        try {
            userService.updateUsername(principal.getName(), newUsername);
            return ResponseEntity.status(HttpStatus.OK).body("Username changed for user with name" + principal.getName());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("User with this username already exists");
        }
    }

    //todo подумать над тем надо ли будет разлогинить пользователя после смены пароля и как организовать саму смену
//    @PostMapping("/change-password")
//    public ResponseEntity<String> changePassword(@RequestBody String newPassword, Principal principal) {
//        try {
//            User user = userService.getByUsername(principal.getName());
//            passwordService.generateToken(user.getEmail());
//            return ResponseEntity.ok("Password changed successfully");
//        } catch (IllegalArgumentException e) {
//            return null;
//        }
//    }

    @PostMapping("/change-email")
    public ResponseEntity<String> changeEmail() {
        return null;
    }

    @PostMapping("add-phone-number")
    public ResponseEntity<String> addPhoneNumber() {
        return null;
    }

}
