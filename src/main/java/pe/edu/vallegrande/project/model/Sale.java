package pe.edu.vallegrande.project.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Sales", nullable = false)
    private Long idSales;

    @Column(name = "id_Employee", nullable = false)
    private Long idEmployee;

    @Column(name = "id_Customer", nullable = false)
    private Long idCustomer;

    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    @Column(name = "Total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SaleDetail> details;
}
