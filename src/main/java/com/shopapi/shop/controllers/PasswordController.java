package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.UserFieldsRequestDTO;
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


    //todo подумать над добавлением сюда простой смены пароля и надо ли вообще простую (наверное не надо)
    //todo подумать про то как передавать параметры в виде DTO или по отдельности (лучше с DTO)


    @PostMapping("/generate-password-token")
    public ResponseEntity<String> generatePasswordToken(@RequestBody UserFieldsRequestDTO resetDTO)  {
        try {
            return ResponseEntity.ok(passwordService.generateToken(resetDTO.email()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to generate password token");
        }
    }

    @PostMapping("/check-token")
    public ResponseEntity<String> checkPasswordToken(@RequestBody UserFieldsRequestDTO resetDTO) {
        try {
            return ResponseEntity.ok(passwordService.checkPasswordToken(resetDTO.token()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //todo дописать логаут из системы
    @PostMapping("/new-password")
    public ResponseEntity<String> saveNewPassword(@RequestBody UserFieldsRequestDTO resetDTO) {
        try {
            passwordService.saveNewPassword(resetDTO.email(), resetDTO.newPassword());
            return ResponseEntity.ok().body("Password changed successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to set new password:" + e.getMessage());
        }
    }
}
