package pe.edu.vallegrande.project.service;

import pe.edu.vallegrande.project.model.Store;
import java.util.List;

public interface StoreService {
    List<Store> findAll();
    Store findById(Long id);
    Store save(Store store);
    Store update(Long id, Store store);
    void delete(Long id);
}
