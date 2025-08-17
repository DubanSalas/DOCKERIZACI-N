package pe.edu.vallegrande.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import pe.edu.vallegrande.project.dto.DashboardSummaryDTO;
import pe.edu.vallegrande.project.dto.DashboardSummaryDTO.ClientSummary;
import pe.edu.vallegrande.project.dto.DashboardSummaryDTO.SaleSummary;
import pe.edu.vallegrande.project.dto.DashboardSummaryDTO.ProductStockAlert;
import pe.edu.vallegrande.project.model.Customer;
import pe.edu.vallegrande.project.model.Sale;
import pe.edu.vallegrande.project.model.Product;
import pe.edu.vallegrande.project.service.DashboardService;
import pe.edu.vallegrande.project.repository.CustomerRepository;
import pe.edu.vallegrande.project.repository.EmployeeRepository;
import pe.edu.vallegrande.project.repository.ProductRepository;
import pe.edu.vallegrande.project.repository.SaleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    private static final int LOW_STOCK_THRESHOLD = 5;

    @Override
    public DashboardSummaryDTO getDashboardSummary() {
        DashboardSummaryDTO dto = new DashboardSummaryDTO();

        LocalDate today = LocalDate.now();

        // KPIs ventas
        dto.setTotalSalesDay(saleRepository.countByDate(today));
        dto.setTotalSalesMonth(saleRepository.countByMonth(today.getYear(), today.getMonthValue()));
        dto.setTotalSalesYear(saleRepository.countByYear(today.getYear()));

        // Total productos en inventario (evitar NullPointerException)
        Integer totalInStock = productRepository.countTotalInStock();
        dto.setTotalProductsInInventory(totalInStock != null ? totalInStock : 0);

        dto.setTotalActiveClients(customerRepository.countActive());
        dto.setTotalActiveEmployees(employeeRepository.countActive());

        // Productos con stock crítico
        List<Product> lowStockProducts = productRepository.findByStatus("active").stream()
            .filter(p -> p.getStock() != null && p.getStock() <= LOW_STOCK_THRESHOLD)
            .collect(Collectors.toList());

        List<ProductStockAlert> lowStockAlerts = lowStockProducts.stream()
            .map(product -> {
                ProductStockAlert alert = new ProductStockAlert();
                alert.setProductId(product.getIdProduct());
                alert.setProductName(product.getProductName());
                alert.setStockQuantity(product.getStock());
                return alert;
            })
            .collect(Collectors.toList());

        dto.setCriticalStockProducts(lowStockAlerts);

        // Últimas ventas
        List<Sale> recentSalesEntities = saleRepository.findLastSales(PageRequest.of(0, 5));
        List<SaleSummary> recentSalesDTO = recentSalesEntities.stream()
            .map(sale -> {
                SaleSummary summary = new SaleSummary();
                summary.setSaleId(sale.getIdSales());

                Customer client = customerRepository.findById(sale.getIdCustomer()).orElse(null);
                if (client != null) {
                    summary.setClientName(client.getName() + " " + client.getSurname());
                } else {
                    summary.setClientName("Unknown");
                }

                summary.setSaleDate(sale.getSaleDate() != null ? sale.getSaleDate().toString() : "");
                summary.setTotalAmount(sale.getTotal() != null ? sale.getTotal().doubleValue() : 0.0);
                return summary;
            })
            .collect(Collectors.toList());

        dto.setRecentSales(recentSalesDTO);

        // Últimos clientes
        List<Customer> recentClientsEntities = customerRepository.findLastClients(PageRequest.of(0, 5));
        List<ClientSummary> recentClientsDTO = recentClientsEntities.stream()
            .map(client -> {
                ClientSummary cs = new ClientSummary();
                cs.setClientId(client.getIdCustomer());
                cs.setClientName(client.getName() + " " + client.getSurname());
                cs.setRegistrationDate(client.getDateBirth() != null ? client.getDateBirth().toString() : "");
                return cs;
            })
            .collect(Collectors.toList());

        dto.setRecentClients(recentClientsDTO);

        return dto;
    }
}
