package com.shopapi.shop.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class AbstractService<T, ID> implements GenericService<T, ID> {
    private final JpaRepository<T, ID> repository;

    protected AbstractService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public T getById(ID id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    public List<T> getAll () {
        return repository.findAll();
    }

    @Transactional
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

}
