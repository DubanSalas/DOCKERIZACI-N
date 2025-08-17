package pe.edu.vallegrande.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.ClassPathResource;
import pe.edu.vallegrande.project.model.Product;
import pe.edu.vallegrande.project.model.Sale;
import pe.edu.vallegrande.project.model.SaleDetail;
import pe.edu.vallegrande.project.repository.ProductRepository;
import pe.edu.vallegrande.project.repository.SaleDetailRepository;
import pe.edu.vallegrande.project.repository.SaleRepository;
import pe.edu.vallegrande.project.service.SaleService;

import net.sf.jasperreports.engine.*;
import javax.sql.DataSource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final ProductRepository productRepository;
    private final DataSource dataSource;

    @Override
    @Transactional
    public Sale saveSaleWithDetails(Sale sale) {
        BigDecimal totalSale = BigDecimal.ZERO;

        // Iterate over SaleDetails, calculate price and subtotal for each item
        for (SaleDetail detail : sale.getDetails()) {
            detail.setSale(sale);

            // Fetch product by ID to set the price for the sale detail
            Product product = productRepository.findById(detail.getIdProduct())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + detail.getIdProduct()));

            detail.setPrice(product.getPrice());

            // Calculate the subtotal for the product and accumulate it in totalSale
            BigDecimal subtotal = BigDecimal.valueOf(detail.getAmount()).multiply(detail.getPrice());
            totalSale = totalSale.add(subtotal);
        }

        // Set the total sale amount
        sale.setTotal(totalSale);

        // Save the sale and return it
        return saleRepository.save(sale);
    }

    @Override
    public List<Sale> getAllSales() {
        // Return all sales from the repository
        return saleRepository.findAll();
    }

    @Override
    public List<SaleDetail> getDetailsBySaleId(Long saleId) {
        // Fetch SaleDetails by saleId using the repository
        return saleDetailRepository.findBySale_IdSales(saleId);
    }

    @Override
    @Transactional
    public Sale updateSaleWithDetails(Long saleId, Sale updatedSale) {
        // Fetch the existing sale
        Sale existingSale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + saleId));

        // Update sale properties
        existingSale.setIdCustomer(updatedSale.getIdCustomer());
        existingSale.setIdEmployee(updatedSale.getIdEmployee());
        existingSale.setSaleDate(updatedSale.getSaleDate());
        existingSale.setPaymentMethod(updatedSale.getPaymentMethod());

        // Remove existing SaleDetails and clear the list
        List<SaleDetail> currentDetails = saleDetailRepository.findBySale_IdSales(saleId);
        saleDetailRepository.deleteAllInBatch(currentDetails);
        existingSale.getDetails().clear();

        // Recalculate the total sale amount based on updated SaleDetails
        BigDecimal totalSale = BigDecimal.ZERO;
        for (SaleDetail newDetail : updatedSale.getDetails()) {
            newDetail.setSale(existingSale);  // Associate SaleDetail with the existing sale
            newDetail.setIdSaleDetail(null);  // Reset ID to ensure a new entry is created

            BigDecimal subtotal = BigDecimal.valueOf(newDetail.getAmount()).multiply(newDetail.getPrice());
            totalSale = totalSale.add(subtotal);

            // Save the new SaleDetail
            saleDetailRepository.save(newDetail);
        }

        // Set the updated total in the sale entity
        existingSale.setTotal(totalSale);

        // Save and return the updated sale
        return saleRepository.save(existingSale);
    }

    @Override
    @Transactional
    public Sale updateSaleOnly(Long saleId, Sale updatedSale) {
        // Fetch the existing sale
        Sale existingSale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + saleId));

        // Update only sale properties (excluding SaleDetails)
        existingSale.setIdCustomer(updatedSale.getIdCustomer());
        existingSale.setIdEmployee(updatedSale.getIdEmployee());
        existingSale.setSaleDate(updatedSale.getSaleDate());
        existingSale.setPaymentMethod(updatedSale.getPaymentMethod());

        // Save and return the updated sale without updating the details
        return saleRepository.save(existingSale);
    }

    @Override
    public byte[] generateJasperPdfReport() throws Exception {
        // Cargar archivo .jasper en src/main/resources/reports (SIN USAR IMÁGENES EN EL JASPER)
        InputStream jasperStream = new ClassPathResource("reports/Ventas.jasper").getInputStream();

        // Sin parámetros
        HashMap<String, Object> params = new HashMap<>();
        // Llenar reporte con conexión a Oracle Cloud con application.yml | aplicación.properties
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource.getConnection());

        // Exportar a PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] generateJasperPdfReportBySaleId(Long saleId) throws Exception {
        // Cargar archivo .jasper correspondiente al reporte
        InputStream jasperStream = new ClassPathResource("reports/VentasReport.jasper").getInputStream();

        // Pasar el saleId como parámetro
        HashMap<String, Object> params = new HashMap<>();
        params.put("saleId", saleId); // Este es el parámetro que se usará en el reporte

        // Llenar el reporte con la conexión a la base de datos
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource.getConnection());

        // Exportar el reporte como un PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
