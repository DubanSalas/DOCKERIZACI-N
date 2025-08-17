package pe.edu.vallegrande.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.project.model.SaleDetail;

import java.util.List;

public interface SaleDetailRepository extends JpaRepository<SaleDetail, Long> {

    List<SaleDetail> findBySale_IdSales(Long saleId);
}
