package pe.edu.vallegrande.project.service;

import pe.edu.vallegrande.project.model.Sale;
import pe.edu.vallegrande.project.model.SaleDetail;

import java.util.List;

public interface SaleService {

    // Método para guardar la venta con los detalles
    Sale saveSaleWithDetails(Sale sale);

    // Método para obtener todas las ventas
    List<Sale> getAllSales();

    // Método para obtener los detalles de una venta por su ID
    List<SaleDetail> getDetailsBySaleId(Long saleId);

    // Método para actualizar la venta con sus detalles
    Sale updateSaleWithDetails(Long saleId, Sale sale);

    // Método para actualizar solo la venta sin modificar los detalles
    Sale updateSaleOnly(Long saleId, Sale sale);

    // Método para generar el reporte PDF usando JasperReports (sin parámetros)
    byte[] generateJasperPdfReport() throws Exception;

    // Método para generar el reporte PDF de una venta específica usando JasperReports
    byte[] generateJasperPdfReportBySaleId(Long saleId) throws Exception;
}
