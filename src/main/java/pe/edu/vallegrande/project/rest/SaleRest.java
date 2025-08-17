package pe.edu.vallegrande.project.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.project.model.Sale;
import pe.edu.vallegrande.project.model.SaleDetail;
import pe.edu.vallegrande.project.service.SaleService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/api/sales")
@RequiredArgsConstructor
public class SaleRest {

    private final SaleService saleService;

    @PostMapping
    public Sale createSale(@RequestBody Sale sale) {
        return saleService.saveSaleWithDetails(sale);
    }

    @GetMapping
    public List<Sale> getAllSales() {
        return saleService.getAllSales();
    }

    @PutMapping("/{saleId}/info")
    public Sale updateSaleInfo(@PathVariable Long saleId, @RequestBody Sale sale) {
        return saleService.updateSaleOnly(saleId, sale);
    }

    @PutMapping("/{saleId}")
    public Sale updateSale(@PathVariable Long saleId, @RequestBody Sale sale) {
        return saleService.updateSaleWithDetails(saleId, sale);
    }

    @GetMapping("/{saleId}/details")
    public List<SaleDetail> getSaleDetails(@PathVariable Long saleId) {
        return saleService.getDetailsBySaleId(saleId);
    }

    // Endpoint para generar el reporte PDF de ventas filtrado por saleId
    @GetMapping("/pdf/{saleId}")
    public ResponseEntity<byte[]> generateJasperPdfReport(@PathVariable Long saleId) {
        try {
            // Llamando al servicio para generar el PDF por saleId
            byte[] pdf = saleService.generateJasperPdfReportBySaleId(saleId); 

            // Renombrar el archivo PDF al descargar
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_venta_" + saleId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build(); // En caso de error, se retorna un error 500
        }
    }
}
