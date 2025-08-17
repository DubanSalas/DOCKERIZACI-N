package pe.edu.vallegrande.project.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.vallegrande.project.model.Store;
import pe.edu.vallegrande.project.service.StoreService;

@CrossOrigin(origins = "*") // Cambia el origen según sea necesario
@RestController
@RequestMapping("/api/stores")
public class StoreRest {

    @Autowired
    private StoreService storeService;

    @GetMapping
    public List<Store> getAllStores() {
        return storeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
        Store store = storeService.findById(id);
        if (store == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(store);
    }

    @PostMapping
    public Store createStore(@RequestBody Store store) {
        return storeService.save(store);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Store> updateStore(@PathVariable Long id, @RequestBody Store storeDetails) {
        Store updatedStore = storeService.update(id, storeDetails);
        if (updatedStore == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStore);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
