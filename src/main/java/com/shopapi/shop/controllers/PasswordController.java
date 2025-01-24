package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.UserFieldsRequestDTO;
import com.shopapi.shop.impl.PasswordServiceImpl;
import com.shopapi.shop.impl.UserServiceImpl;
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
    private final UserServiceImpl userService;

    public PasswordController(PasswordServiceImpl passwordService,
                              UserServiceImpl userService) {
        this.passwordService = passwordService;
        this.userService = userService;
    }


    //todo подумать над добавлением сюда простой смены пароля и надо ли вообще простую (наверное не надо)
    //todo подумать про то как передавать параметры в виде DTO или по отдельности (лучше с DTO)


    @PostMapping("/generate-password-token")
    public ResponseEntity<String> generatePasswordToken(@RequestBody UserFieldsRequestDTO resetDTO)  {
        try {
            return ResponseEntity.ok(passwordService.generateToken(resetDTO.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to generate password token");
        }
    }

    //todo добавить валидацию для совпадения что токен конкретно какого то пользователя
    @PostMapping("/check-token")
    public ResponseEntity<String> checkPasswordToken(@RequestBody UserFieldsRequestDTO resetDTO) {
        try {
            return ResponseEntity.ok(passwordService.checkPasswordToken(resetDTO.getToken()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/new-password")
    public ResponseEntity<String> saveNewPassword(@RequestBody UserFieldsRequestDTO resetDTO) {
        try {
            passwordService.saveNewPassword(resetDTO.getEmail(), resetDTO.getNewPassword());
            return ResponseEntity.ok().body("Password changed successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to set new password:" + e.getMessage());
        }
    }
}
