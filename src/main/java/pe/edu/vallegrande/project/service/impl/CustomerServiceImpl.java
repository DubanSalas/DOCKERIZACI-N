package pe.edu.vallegrande.project.service.impl;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.project.model.Customer;
import pe.edu.vallegrande.project.repository.CustomerRepository;
import pe.edu.vallegrande.project.service.CustomerService;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final DataSource dataSource;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, DataSource dataSource) {
        this.customerRepository = customerRepository;
        this.dataSource = dataSource;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findByClientCode(String clientCode) {
        return customerRepository.findByClientCode(clientCode);
    }

    @Override
    public List<Customer> findByStatus(String status) {
        return customerRepository.findByStatus(status);
    }

    @Override
    public Customer save(Customer customer) {
        try {
            // Validación de campos obligatorios
            if (customer.getName() == null || customer.getDocumentNumber() == null) {
                throw new IllegalArgumentException("El nombre y el número de documento son obligatorios.");
            }

            // Validación de teléfono
            if (customer.getPhone() != null && customer.getPhone().length() > 9) {
                throw new IllegalArgumentException("Número de teléfono inválido.");
            }

            // Establecer el estado por defecto a 'A' (activo)
            customer.setStatus("A");

            // Generar el clientCode si no existe
            if (customer.getClientCode() == null || customer.getClientCode().isEmpty()) {
                customer.setClientCode(generateClientCode());
            }

            // Guardar cliente
            return customerRepository.save(customer);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error en los datos del cliente: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public Customer update(String clientCode, Customer customer) {
        Optional<Customer> existing = customerRepository.findByClientCode(clientCode);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado.");
        }

        // Actualizar los campos del cliente
        Customer updatedCustomer = existing.get();
        updatedCustomer.setDocumentType(customer.getDocumentType());
        updatedCustomer.setDocumentNumber(customer.getDocumentNumber());
        updatedCustomer.setName(customer.getName());
        updatedCustomer.setSurname(customer.getSurname());
        updatedCustomer.setEmail(customer.getEmail());
        updatedCustomer.setPhone(customer.getPhone());
        updatedCustomer.setAddress(customer.getAddress());
        updatedCustomer.setDateBirth(customer.getDateBirth());

        if (customer.getStatus() != null) {
            updatedCustomer.setStatus(customer.getStatus());
        }

        if (customer.getRole() != null) {
            updatedCustomer.setRole(customer.getRole());
        }

        // Guardar el cliente actualizado
        return customerRepository.save(updatedCustomer);
    }

    @Override
    public void delete(String clientCode) {
        customerRepository.findByClientCode(clientCode).ifPresent(c -> {
            c.setStatus("I");  // Cambiar el estado a inactivo
            customerRepository.save(c);
        });
    }

    @Override
    public void restore(String clientCode) {
        customerRepository.findByClientCode(clientCode).ifPresent(c -> {
            c.setStatus("A");  // Restaurar el estado a activo
            customerRepository.save(c);
        });
    }

    @Override
    public byte[] generateJasperPdfReport() throws Exception {
        try {
            InputStream jasperStream = new ClassPathResource("reports/CustomerReport.jasper").getInputStream();
            HashMap<String, Object> params = new HashMap<>();
            params.put("ReportTitle", "Listado de Clientes");

            // Llenar el reporte con los datos de la base de datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource.getConnection());
            return JasperExportManager.exportReportToPdf(jasperPrint); // Exportar el reporte a PDF
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte en PDF: " + e.getMessage(), e);
        }
    }

    // Método para generar un clientCode único
    private String generateClientCode() {
        // Obtener el último clientCode para generar el siguiente
        Optional<String> latestClientCode = customerRepository.findLatestClientCode();
        if (latestClientCode.isPresent()) {
            String lastCode = latestClientCode.get();
            int lastNumber = Integer.parseInt(lastCode.substring(1)); // Asume que el código sigue el formato 'C0001'
            lastNumber++;
            return "C" + String.format("%04d", lastNumber);  // Generar el siguiente código en formato C0002, C0003, etc.
        } else {
            return "C0001";  // Si no existen registros, retornar C0001
        }
    }
}
