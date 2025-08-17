package pe.edu.vallegrande.project.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.vallegrande.project.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.saleDate = :date")
    long countByDate(LocalDate date);

    @Query("SELECT COUNT(s) FROM Sale s WHERE YEAR(s.saleDate) = :year AND MONTH(s.saleDate) = :month")
    long countByMonth(int year, int month);

    @Query("SELECT COUNT(s) FROM Sale s WHERE YEAR(s.saleDate) = :year")
    long countByYear(int year);

    @Query("SELECT s FROM Sale s ORDER BY s.saleDate DESC")
    List<Sale> findLastSales(Pageable pageable);
}
