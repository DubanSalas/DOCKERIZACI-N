package pe.edu.vallegrande.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.project.model.Employee;
import pe.edu.vallegrande.project.repository.EmployeeRepository;
import pe.edu.vallegrande.project.service.EmployeeService;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public List<Employee> findByStatus(String status) {
        return employeeRepository.findByStatus(status);
    }

    @Override
    public Employee save(Employee employee) {
        try {
            System.out.println(">>> Intentando guardar empleado: " + employee);

            if (employee.getName() == null || employee.getSurname() == null || employee.getPost() == null) {
                throw new IllegalArgumentException("✖ El nombre, apellido y cargo son obligatorios.");
            }

            if (employee.getPhone() != null && employee.getPhone().length() > 9) {
                throw new IllegalArgumentException("✖ Número de teléfono inválido.");
            }

            employee.setStatus("A");
            Employee saved = employeeRepository.save(employee);
            System.out.println("✔ Empleado guardado exitosamente con ID: " + saved.getIdEmployee());
            return saved;

        } catch (Exception e) {
            System.err.println("⛔ ERROR al guardar empleado: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Employee update(Long id, Employee employee) {
        try {
            System.out.println(">>> Intentando actualizar empleado con ID: " + id);
            Optional<Employee> optional = employeeRepository.findById(id);
            if (optional.isEmpty()) {
                throw new IllegalArgumentException("Empleado no encontrado con ID: " + id);
            }

            Employee existing = optional.get();

            existing.setName(employee.getName());
            existing.setSurname(employee.getSurname());
            existing.setPost(employee.getPost());
            existing.setPhone(employee.getPhone());

            if (employee.getStatus() != null) {
                existing.setStatus(employee.getStatus());
            }

            Employee updated = employeeRepository.save(existing);
            System.out.println("✔ Empleado actualizado con ID: " + updated.getIdEmployee());
            return updated;

        } catch (Exception e) {
            System.err.println("⛔ Error al actualizar empleado: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delete(Long id) {
        employeeRepository.findById(id).ifPresent(e -> {
            e.setStatus("I");
            employeeRepository.save(e);
        });
    }

    @Override
    public void restore(Long id) {
        employeeRepository.findById(id).ifPresent(e -> {
            e.setStatus("A");
            employeeRepository.save(e);
        });
    }
}
