package pe.edu.vallegrande.project.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Product", nullable = false)
    private Long idProduct;

    @Column(name = "id_Store", nullable = false)
    private Long storeIdStore;

    @Column(name = "Product_Name", nullable = false, length = 100)
    private String productName;

    @Column(name = "Description", length = 100)
    private String description;

    @Column(name = "Price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "Status", nullable = false, length = 1)
    private String status = "A";

    @Column(name = "Type", length = 50)
    private String type;

    @Column(name = "image", length = 255)
    private String image;
}
