package pe.edu.vallegrande.project.service;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.vallegrande.project.model.Product;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    List<Product> findByStatus(String status);
    List<Product> findByStatusAndType(String status, String type);

    Product save(String productName, String description, String price, int stock, String status, String type, Long storeIdStore, MultipartFile imageFile) throws IOException;

    Product update(Long id, String productName, String description, String price, int stock, String status, String type, Long storeIdStore, MultipartFile imageFile) throws IOException;

    void delete(Long id);
    void restore(Long id);

    String storeProductImage(Long productId, MultipartFile file) throws IOException;

    // Nuevo método para validar existencia por nombre
    boolean existsByProductName(String productName);

    // Nuevo método para generar reporte PDF Jasper
    byte[] generateJasperPdfReport() throws Exception;
}
