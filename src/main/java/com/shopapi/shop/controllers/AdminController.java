package com.shopapi.shop.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/admin/v1")
public class AdminController {

    //todo
    @GetMapping("/username")
    public String getUsername(Principal principal) {
        return principal.getName();
    }
}
