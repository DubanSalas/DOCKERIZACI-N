package pe.edu.vallegrande.project.repository;

import pe.edu.vallegrande.project.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Buscar clientes activos (status = "A")
    List<Customer> findByStatus(String status);

    // Buscar clientes cuyo estado NO sea el indicado (por ejemplo, inactivos)
    List<Customer> findByStatusNot(String status);

    // Buscar clientes por email
    List<Customer> findByEmail(String email);

    // Buscar clientes por número de documento
    List<Customer> findByDocumentNumber(String documentNumber);

    // Buscar cliente por ClientCode
    Optional<Customer> findByClientCode(String clientCode);

    // Buscar cliente por documento y tipo (mejor validación)
    Optional<Customer> findByDocumentNumberAndDocumentType(String documentNumber, String documentType);

    // Buscar todos los clientes ordenados por el nombre o alguna otra propiedad (si es necesario)
    List<Customer> findAllByOrderByNameAsc();

    // 📄 Obtener el último clientCode (suposición: orden por ID de cliente)
    @Query("SELECT c.clientCode FROM Customer c ORDER BY c.idCustomer DESC")
    Optional<String> findLatestClientCode();

    // Obtener clientes por estado con paginación
    List<Customer> findByStatus(String status, Pageable pageable);

    // =============================
    // MÉTODOS AGREGADOS PARA DASHBOARD
    // =============================

    // Contar clientes activos (status = 'A')
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.status = 'A'")
    long countActive();

    // Obtener últimos clientes activos con paginación (orden descendente)
    @Query("SELECT c FROM Customer c WHERE c.status = 'A' ORDER BY c.idCustomer DESC")
    List<Customer> findLastClients(Pageable pageable);
}
