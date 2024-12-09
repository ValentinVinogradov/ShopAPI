package com.shopapi.shop.controller;

import com.shopapi.shop.impl.UserServiceImpl;
import com.shopapi.shop.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop_api/v1/users")
public class UserController extends GenericController<User, Long> {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        super(userService);
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully!");
    }

    @PatchMapping("/{id}/username")
    public ResponseEntity<String> updateUsername(
            @PathVariable("id") Long userId,
            @RequestBody String username) {

        userService.updateUsername(userId, username);
        return ResponseEntity.ok("Username updated successfully!");
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(
            @PathVariable("id") Long userId,
            @RequestBody String password) {

        userService.updatePassword(userId, password);
        return ResponseEntity.ok("Password updated successfully!");
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<String> updateEmail(
            @PathVariable("id") Long userId,
            @RequestBody String email) {

        userService.updateEmail(userId, email);
        return ResponseEntity.ok("Email updated successfully!");
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user); // 200 OK с объектом User
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // 404 Not Found с сообщением
        }
    }

//    //todo доработать
//    @GetMapping("/exists/{email}")
//    public ResponseEntity<String> userExists(@PathVariable String email) {
//        boolean exists = userService.userExists(email);
//        if (exists) {
//            return ResponseEntity.ok("User with email " + email + " exists."); // 200 OK с сообщением
//        } else {
//            return ResponseEntity.ok("User with email " + email + " does not exist."); // 200 OK с сообщением
//        }
//    }
}
