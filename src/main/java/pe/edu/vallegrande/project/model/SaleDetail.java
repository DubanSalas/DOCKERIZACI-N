package pe.edu.vallegrande.project.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "sale_detail")
public class SaleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Sale_Detail", nullable = false)
    private Long idSaleDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_Sales", nullable = false)
    @JsonIgnore
    private Sale sale;

    @Column(name = "id_Product", nullable = false)
    private Long idProduct;

    @Column(name = "Amount", nullable = false)
    private Integer amount;

    @Column(name = "Price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "Total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
}
