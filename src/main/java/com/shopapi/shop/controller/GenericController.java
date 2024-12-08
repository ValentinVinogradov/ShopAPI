package com.shopapi.shop.controller;

import com.shopapi.shop.services.GenericService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class GenericController<T, ID> {
    public final GenericService<T, ID> service;

    public GenericController(GenericService<T, ID> service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable ID id) {
        T entity = service.getById(id);
        if (entity != null) {
            return ResponseEntity.ok(entity); // Статус 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Статус 404 Not Found
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<T>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable ID id) {
        service.deleteById(id);
        return ResponseEntity.ok("Deleted successfully"); // Статус 200 OK
    }

}
