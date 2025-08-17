package pe.edu.vallegrande.project.rest;

import pe.edu.vallegrande.project.model.Supplier;
import pe.edu.vallegrande.project.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/api/supplier")
public class SupplierRest {

    private final SupplierService supplierService;

    @Autowired
    public SupplierRest(SupplierService supplierService){
        this.supplierService = supplierService;
    }

    @GetMapping
    public List<Supplier> findAll(){
        return supplierService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Supplier> findById(@PathVariable Long id){
        return supplierService.findById(id);
    }

    @GetMapping("/status/{status}")
    public List<Supplier> findByStatus(@PathVariable String status){
        return supplierService.findByStatus(status);
    }

    @PostMapping("/save")
    public Supplier save(@RequestBody Supplier supplier){
        return supplierService.save(supplier);
    }

    @PutMapping("/update/{id}")
    public Supplier update(@PathVariable Long id, @RequestBody Supplier supplier){
        return supplierService.update(id, supplier);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        supplierService.delete(id);
    }

    @PutMapping("/restore/{id}")
    public void restore(@PathVariable Long id){
        supplierService.restore(id);
    }
}
