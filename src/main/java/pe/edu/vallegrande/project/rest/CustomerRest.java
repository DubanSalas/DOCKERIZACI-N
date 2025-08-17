package pe.edu.vallegrande.project.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.project.model.Customer;
import pe.edu.vallegrande.project.service.CustomerService;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/api/customer")
public class CustomerRest {

    private final CustomerService customerService;

    public CustomerRest(CustomerService customerService) {
        this.customerService = customerService;
    }

    // 🔍 Obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
        List<Customer> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    // 🔍 Obtener cliente por ClientCode
    @GetMapping("/clientCode/{clientCode}")
    public ResponseEntity<Customer> findByClientCode(@PathVariable String clientCode) {
        Optional<Customer> customer = customerService.findByClientCode(clientCode);
        return customer.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔍 Obtener clientes por estado (A o I)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Customer>> findByStatus(@PathVariable String status) {
        List<Customer> customers = customerService.findByStatus(status);
        return ResponseEntity.ok(customers);
    }

    // ✅ Crear nuevo cliente
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Customer customer) {
        // Validación para asegurarse de que los campos obligatorios estén presentes
        if (isMissingRequiredFields(customer)) {
            return ResponseEntity.badRequest().body("✖ Faltan campos obligatorios: nombre, tipo o número de documento.");
        }

        customer.setStatus("A");  // Establecer el estado por defecto como activo

        // Generar el clientCode si no existe
        if (customer.getClientCode() == null || customer.getClientCode().isEmpty()) {
            customer.setClientCode(generateClientCode());
        }

        Customer saved = customerService.save(customer);
        return ResponseEntity.ok(saved);  // Retornar el cliente guardado con estado 200 OK
    }

    // ✏️ Actualizar cliente
    @PutMapping("/update/{clientCode}")
    public ResponseEntity<?> update(@PathVariable String clientCode, @RequestBody Customer customer) {
        Optional<Customer> existing = customerService.findByClientCode(clientCode);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();  // Si el cliente no existe, retornar 404 Not Found
        }

        // Validación de campos obligatorios
        if (isMissingRequiredFields(customer)) {
            return ResponseEntity.badRequest().body("✖ Faltan campos obligatorios para actualizar.");
        }

        Customer updated = customerService.update(clientCode, customer);  // Actualizar el cliente
        return ResponseEntity.ok(updated);  // Retornar el cliente actualizado con estado 200 OK
    }

    // ❌ Eliminar lógicamente (usar clientCode)
    @PatchMapping("/delete/{clientCode}")
    public ResponseEntity<Void> delete(@PathVariable String clientCode) {
        Optional<Customer> existing = customerService.findByClientCode(clientCode);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();  // Si el cliente no existe, retornar 404 Not Found
        }
        customerService.delete(clientCode);  // Eliminar (inactivar) el cliente lógicamente
        return ResponseEntity.noContent().build();  // Retornar 204 No Content (sin contenido)
    }

    // ♻️ Restaurar cliente (usar clientCode)
    @PutMapping("/restore/{clientCode}")
    public ResponseEntity<Void> restore(@PathVariable String clientCode) {
        Optional<Customer> existing = customerService.findByClientCode(clientCode);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();  // Si el cliente no existe, retornar 404 Not Found
        }
        customerService.restore(clientCode);  // Restaurar el cliente (cambiar su estado a activo)
        return ResponseEntity.noContent().build();  // Retornar 204 No Content (sin contenido)
    }

    // 📄 Generar y descargar PDF de clientes
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generateJasperPdfReport() {
        try {
            byte[] pdf = customerService.generateJasperPdfReport();  // Generar el reporte en PDF
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_clientes.pdf")  // Forzar descarga del archivo
                    .contentType(MediaType.APPLICATION_PDF)  // Establecer tipo de contenido como PDF
                    .body(pdf);  // Enviar el PDF como respuesta
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);  // Si hay un error, retornar 500 con mensaje
        }
    }

    // 🔥 Captura errores generales y devuelve mensaje legible
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        ex.printStackTrace();  // Muestra el error completo en consola para debugging
        return ResponseEntity.status(500).body("❌ Error interno del servidor: " + ex.getMessage());  // Retornar error 500 con mensaje
    }

    // Método auxiliar para verificar campos obligatorios
    private boolean isMissingRequiredFields(Customer customer) {
        return customer.getName() == null || customer.getDocumentNumber() == null || customer.getDocumentType() == null;
    }

    // Método auxiliar para generar el ClientCode automáticamente
    private String generateClientCode() {
        // Aquí se debe implementar la lógica para generar un código único para el cliente
        return "C" + String.format("%04d", (int) (Math.random() * 10000)); // Generar un código de cliente aleatorio de 4 dígitos
    }
}
