package pe.edu.vallegrande.project.repository;

import pe.edu.vallegrande.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatus(String status);
    List<Product> findByStatusNot(String status);
    List<Product> findByStatusAndType(String status, String type);

    boolean existsByProductNameIgnoreCase(String productName);

    // Total stock sum of products with status = 'active' (o el que uses)
    @Query("SELECT SUM(p.stock) FROM Product p WHERE p.status = 'active'")
    Integer countTotalInStock();

    // Find product names with stock less than or equal a threshold and active
    @Query("SELECT p.productName FROM Product p WHERE p.stock <= :threshold AND p.status = 'active'")
    List<String> findProductsWithLowStock(int threshold);
}
