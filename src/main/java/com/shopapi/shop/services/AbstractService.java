package com.shopapi.shop.services;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@AllArgsConstructor
public abstract class AbstractService<T, ID> implements GenericService<T, ID> {
    private final JpaRepository<T, ID> repository;

    public List<T> getAll () {
        return repository.findAll();
        //todo условие на null
    }

    public T getById(ID id) {
        return repository.findById(id).orElse(null);
    }

    public void add(T entity) {
        repository.save(entity);
    }

    public void update(T entity) {
        repository.save(entity);
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

}
