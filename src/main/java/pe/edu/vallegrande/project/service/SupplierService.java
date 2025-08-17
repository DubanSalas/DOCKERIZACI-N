package pe.edu.vallegrande.project.service;

import pe.edu.vallegrande.project.model.Supplier;
import java.util.List;
import java.util.Optional;

public interface SupplierService {
    List<Supplier> findAll();
    Optional<Supplier> findById(Long id);
    List<Supplier> findByStatus(String status);
    Supplier save(Supplier supplier);
    Supplier update(Long id, Supplier supplier);
    void delete(Long id);
    void restore(Long id);
}
