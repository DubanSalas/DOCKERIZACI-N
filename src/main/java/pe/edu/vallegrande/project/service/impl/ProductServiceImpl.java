package pe.edu.vallegrande.project.service.impl;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.vallegrande.project.model.Product;
import pe.edu.vallegrande.project.repository.ProductRepository;
import pe.edu.vallegrande.project.service.ProductService;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Path uploadDir = Paths.get("uploads/products");

    private final DataSource dataSource;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, DataSource dataSource) {
        this.productRepository = productRepository;
        this.dataSource = dataSource;
        createUploadDirIfNotExists();
    }

    private void createUploadDirIfNotExists() {
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de carga", e);
        }
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Override
    public List<Product> findByStatus(String status) {
        return productRepository.findByStatus(status);
    }

    @Override
    public List<Product> findByStatusAndType(String status, String type) {
        return productRepository.findByStatusAndType(status, type);
    }

    @Override
    public Product save(String productName, String description, String price, int stock, String status, String type,
                        Long storeIdStore, MultipartFile imageFile) throws IOException {
        Product product = new Product();
        product.setProductName(productName);
        product.setDescription(description);
        product.setPrice(parsePrice(price));
        product.setStock(stock);
        product.setStatus(status);
        product.setType(type);
        product.setStoreIdStore(storeIdStore);

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveImageFile(imageFile, "product");
            product.setImage(fileName);
        }

        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, String productName, String description, String price, int stock, String status,
                          String type, Long storeIdStore, MultipartFile imageFile) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        product.setProductName(productName);
        product.setDescription(description);
        product.setPrice(parsePrice(price));
        product.setStock(stock);
        product.setStatus(status);
        product.setType(type);
        product.setStoreIdStore(storeIdStore);

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveImageFile(imageFile, "product_" + id);
            product.setImage(fileName);
        }

        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        product.setStatus("I");
        productRepository.save(product);
    }

    @Override
    public void restore(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        product.setStatus("A");
        productRepository.save(product);
    }

    @Override
    public String storeProductImage(Long productId, MultipartFile file) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productId));

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No se proporcionó ningún archivo");
        }

        String fileName = saveImageFile(file, "product_" + productId);
        product.setImage(fileName);
        productRepository.save(product);

        return fileName;
    }

    @Override
    public boolean existsByProductName(String productName) {
        return productRepository.existsByProductNameIgnoreCase(productName);
    }

    // Implementación del nuevo método para generar reporte PDF Jasper
    @Override
    public byte[] generateJasperPdfReport() throws Exception {
        InputStream jasperStream = new ClassPathResource("reports/productRepo.jasper").getInputStream();
        HashMap<String, Object> params = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource.getConnection());
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private String saveImageFile(MultipartFile file, String prefix) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String fileName = prefix + "_" + System.currentTimeMillis() + extension;
        Path targetLocation = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation);

        return fileName;
    }

    private BigDecimal parsePrice(String price) {
        try {
            return new BigDecimal(price);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de precio inválido: " + price);
        }
    }
}
