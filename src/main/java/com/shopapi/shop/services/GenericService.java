package com.shopapi.shop.services;

import java.util.List;

public interface GenericService<T, ID> {
    List<T> getAll();

    T getById(ID id);

    void add(T entity);

    void update(T entity);

    void deleteById(ID id);
}
