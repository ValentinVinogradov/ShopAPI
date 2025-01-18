package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.ResetPasswordRequestDTO;
import com.shopapi.shop.impl.PasswordServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password/v1")
public class PasswordController {
    private final PasswordServiceImpl passwordService;

    public PasswordController(PasswordServiceImpl passwordService) {
        this.passwordService = passwordService;
    }


    //todo подумать над добавлением сюда простой смены пароля и надо ли вообще простую
    //todo подумать про то как передавать параметры в виде DTO или по отдельности


    @PostMapping("/generate-token")
    public ResponseEntity<String> generateToken(@RequestBody ResetPasswordRequestDTO resetDTO)  {
        try {
            return ResponseEntity.ok(passwordService.generateToken(resetDTO.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Email doesn't exists");
        }
    }

    @PostMapping("/check-token")
    public ResponseEntity<String> checkToken(@RequestBody ResetPasswordRequestDTO resetDTO) {
        try {
            return ResponseEntity.ok(passwordService.checkToken(resetDTO.getToken()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/new-password")
    public ResponseEntity<String> saveNewPassword(@RequestBody ResetPasswordRequestDTO resetDTO) {
        try {
            passwordService.saveNewPassword(resetDTO.getEmail(), resetDTO.getNewPassword());
            return ResponseEntity.ok().body("Password changed successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to set new password:" + e.getMessage());
        }
    }
}
