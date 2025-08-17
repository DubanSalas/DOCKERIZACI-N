package pe.edu.vallegrande.project.repository;

import pe.edu.vallegrande.project.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByStatus(String status);

    List<Employee> findByStatusNot(String status);

    // =============================
    // MÉTODOS AGREGADOS PARA DASHBOARD
    // =============================

    // Contar empleados activos (status = 'A')
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.status = 'A'")
    long countActive();

    // Obtener últimos empleados activos con paginación (orden descendente)
    @Query("SELECT e FROM Employee e WHERE e.status = 'A' ORDER BY e.idEmployee DESC")
    List<Employee> findLastEmployees(Pageable pageable);
}
