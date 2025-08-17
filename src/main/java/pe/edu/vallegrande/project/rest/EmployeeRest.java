package pe.edu.vallegrande.project.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.project.model.Employee;
import pe.edu.vallegrande.project.service.EmployeeService;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/api/employee")
public class EmployeeRest {

    private final EmployeeService employeeService;

    public EmployeeRest(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // 🔍 Obtener todos
    @GetMapping
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    // 🔍 Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔍 Obtener por estado
    @GetMapping("/status/{status}")
    public List<Employee> findByStatus(@PathVariable String status) {
        return employeeService.findByStatus(status);
    }

    // ✅ Crear nuevo empleado
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Employee employee) {
        if (employee.getName() == null || employee.getSurname() == null || employee.getPost() == null) {
            return ResponseEntity.badRequest().body("✖ Faltan campos obligatorios: nombre, apellido o cargo.");
        }

        employee.setStatus("A");
        Employee saved = employeeService.save(employee);
        return ResponseEntity.ok(saved);
    }

    // ✏️ Actualizar empleado
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Employee employee) {
        Optional<Employee> existing = employeeService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (employee.getName() == null || employee.getSurname() == null || employee.getPost() == null) {
            return ResponseEntity.badRequest().body("✖ Faltan campos obligatorios para actualizar.");
        }

        Employee updated = employeeService.update(id, employee);
        return ResponseEntity.ok(updated);
    }

    // ❌ Eliminar lógicamente
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Employee> existing = employeeService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ♻️ Restaurar
    @PutMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        Optional<Employee> existing = employeeService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employeeService.restore(id);
        return ResponseEntity.noContent().build();
    }

    // 🔥 Captura errores generales y devuelve mensaje legible
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        ex.printStackTrace(); // Muestra el error completo en consola
        return ResponseEntity.status(500).body("❌ Error interno del servidor: " + ex.getMessage());
    }
}
