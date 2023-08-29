package com.dp.homework.films.service;

import com.dp.homework.films.model.GenericModel;
import com.dp.homework.films.repo.GenericRepository;
import groovy.util.logging.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
public abstract class GenericService<T extends GenericModel> {

    private final GenericRepository<T> repository;


    protected GenericService(GenericRepository<T> repository) {
        this.repository = repository;
    }

    public List<T> listAll(){
        return repository.findAll();
    }

    public Page<T> listAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public T getOne(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Запись с таким id не найдена"));
    }

    public T create(T object){
    object.setCreatedBy("ADMIN");
    object.setCreatedWhen(LocalDateTime.now());
    return repository.save(object);
    }

    public T update(T object) {
        object.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        object.setUpdatedWhen(LocalDateTime.now());
        return repository.save(object);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public List<T> findByCreatedBy(String createdBy) {
        return repository.findByCreatedBy(createdBy);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }



    public void softDelete(Long id) {
        T object = getOne(id);
        object.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        object.setDeleted(true);
        object.setDeletedWhen(LocalDateTime.now());
        update(object);
    }

    public void restore(Long id) {
        T object = getOne(id);
        object.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        object.setDeleted(false);
        object.setDeletedWhen(null);
        update(object);
    }


}
