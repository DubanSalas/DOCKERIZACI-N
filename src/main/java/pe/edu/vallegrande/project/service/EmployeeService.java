package pe.edu.vallegrande.project.service;

import pe.edu.vallegrande.project.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    List<Employee> findByStatus(String status);

    Employee save(Employee employee);

    Employee update(Long id, Employee employee);

    void delete(Long id);   // eliminación lógica (cambiar estado)

    void restore(Long id);  // restaurar lógico (cambiar estado)
}
