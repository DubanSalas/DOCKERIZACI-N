package pe.edu.vallegrande.project.service.impl;

import pe.edu.vallegrande.project.model.Supplier;
import pe.edu.vallegrande.project.repository.SupplierRepository;
import pe.edu.vallegrande.project.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository){
        this.supplierRepository = supplierRepository;
    }

    @Override
    public List<Supplier> findAll(){
        return supplierRepository.findAll();
    }

    @Override
    public Optional<Supplier> findById(Long id){
        return supplierRepository.findById(id);
    }

    @Override
    public List<Supplier> findByStatus(String status){
        return supplierRepository.findByStatus(status);
    }

    @Override
    public Supplier save(Supplier supplier){
        supplier.setStatus("A");
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier update(Long id, Supplier supplier){
        Optional<Supplier> existing = supplierRepository.findById(id);
        if(existing.isPresent()){
            Supplier s = existing.get();
            s.setDocumentType(supplier.getDocumentType());
            s.setDocumentNumber(supplier.getDocumentNumber());
            s.setName(supplier.getName());
            s.setSurname(supplier.getSurname());
            s.setAddress(supplier.getAddress());
            s.setEmail(supplier.getEmail());
            s.setPhone(supplier.getPhone());
            s.setStatus(supplier.getStatus());
            return supplierRepository.save(s);
        }
        return null;
    }

    @Override
    public void delete(Long id){
        Optional<Supplier> existing = supplierRepository.findById(id);
        existing.ifPresent(s -> {
            s.setStatus("I");
            supplierRepository.save(s);
        });
    }

    @Override
    public void restore(Long id){
        Optional<Supplier> existing = supplierRepository.findById(id);
        existing.ifPresent(s -> {
            s.setStatus("A");
            supplierRepository.save(s);
        });
    }
}
