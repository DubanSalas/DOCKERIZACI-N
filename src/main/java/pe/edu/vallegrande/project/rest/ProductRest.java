package pe.edu.vallegrande.project.rest;

import pe.edu.vallegrande.project.model.Product;
import pe.edu.vallegrande.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/api/product")
public class ProductRest {

    private final ProductService productService;

    @Autowired
    public ProductRest(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        try {
            Product product = productService.findById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status/{status}")
    public List<Product> findByStatus(@PathVariable String status) {
        return productService.findByStatus(status);
    }

    @GetMapping("/status/{status}/type/{type}")
    public List<Product> findByStatusAndType(@PathVariable String status, @PathVariable String type) {
        return productService.findByStatusAndType(status, type);
    }

    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<?> save(
            @RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("price") String price,
            @RequestParam("stock") int stock,
            @RequestParam("status") String status,
            @RequestParam("type") String type,
            @RequestParam("storeIdStore") Long storeIdStore,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Product savedProduct = productService.save(productName, description, price, stock, status, type, storeIdStore, image);
            return ResponseEntity.ok(savedProduct);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error de validación: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar archivo: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("price") String price,
            @RequestParam("stock") int stock,
            @RequestParam("status") String status,
            @RequestParam("type") String type,
            @RequestParam("storeIdStore") Long storeIdStore,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Product updatedProduct = productService.update(id, productName, description, price, stock, status, type, storeIdStore, image);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error de validación: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar archivo: " + e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        productService.restore(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadProductImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            String imagePath = productService.storeProductImage(id, file);
            return ResponseEntity.ok(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar la imagen: " + e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    // Nuevo endpoint para generar el reporte PDF Jasper
    @GetMapping("/pdf")
    public ResponseEntity<ByteArrayResource> generateJasperPdfReport() {
        try {
            byte[] pdfData = productService.generateJasperPdfReport();
            ByteArrayResource resource = new ByteArrayResource(pdfData);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_products.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfData.length)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
