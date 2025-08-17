package pe.edu.vallegrande.project.service;

import pe.edu.vallegrande.project.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    /**
     * Obtener todos los clientes.
     * @return una lista de clientes.
     */
    List<Customer> findAll();

    /**
     * Buscar un cliente por su código único (clientCode).
     * @param clientCode el código único del cliente.
     * @return un cliente si se encuentra, de lo contrario, Optional vacío.
     */
    Optional<Customer> findByClientCode(String clientCode);

    /**
     * Buscar clientes por su estado (activo o inactivo).
     * @param status el estado del cliente (A para activo, I para inactivo).
     * @return una lista de clientes con el estado especificado.
     */
    List<Customer> findByStatus(String status);

    /**
     * Crear un nuevo cliente.
     * @param customer el cliente a guardar.
     * @return el cliente guardado con los datos actualizados.
     */
    Customer save(Customer customer);

    /**
     * Actualizar los datos de un cliente por su clientCode.
     * @param clientCode el código único del cliente.
     * @param customer el cliente con los nuevos datos.
     * @return el cliente actualizado.
     */
    Customer update(String clientCode, Customer customer);

    /**
     * Eliminar lógicamente un cliente (cambiar su estado a inactivo) por su clientCode.
     * @param clientCode el código único del cliente.
     */
    void delete(String clientCode);

    /**
     * Restaurar un cliente (cambiar su estado a activo) por su clientCode.
     * @param clientCode el código único del cliente.
     */
    void restore(String clientCode);

    /**
     * Generar un reporte en formato PDF con los datos de los clientes utilizando JasperReports.
     * @return un array de bytes representando el archivo PDF.
     * @throws Exception si ocurre algún error durante la generación del reporte.
     */
    byte[] generateJasperPdfReport() throws Exception;
}
