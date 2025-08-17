package pe.edu.vallegrande.project.repository;

import pe.edu.vallegrande.project.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByStatus(String status);
    List<Supplier> findByStatusNot(String status);
}
