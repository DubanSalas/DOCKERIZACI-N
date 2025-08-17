package pe.edu.vallegrande.project.service.impl;

import pe.edu.vallegrande.project.model.Store;
import pe.edu.vallegrande.project.repository.StoreRepository;
import pe.edu.vallegrande.project.service.StoreService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
    }

    @Override
    public List<Store> findAll(){
        return storeRepository.findAll();
    }

    @Override
    public Store findById(Long id){
        return storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + id));
    }

    @Override
    public Store save(Store store){
        return storeRepository.save(store);
    }

    @Override
    public Store update(Long id, Store store){
        Store existing = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + id));
        existing.setLocation(store.getLocation());
        existing.setResponsible(store.getResponsible());
        return storeRepository.save(existing);
    }

    @Override
    public void delete(Long id){
        storeRepository.deleteById(id);
    }
}
