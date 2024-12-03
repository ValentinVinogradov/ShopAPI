package com.shopapi.shop.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class StatusUtils {

    // Возврат 404, если список пуст, или 200 с телом
    public static <T> ResponseEntity<List<T>> respondWithList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(list);
    }

    // Возврат 404, если объект null, или 200 с объектом
    public static <T> ResponseEntity<T> respondWithObject(Optional<T> optionalObject) {
        return optionalObject.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public static <T> ResponseEntity<T> created(T body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public static ResponseEntity<Object> internalServerError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

}

