package pe.edu.vallegrande.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "Store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Se recomienda usar el mismo nombre que en la BD para claridad
    @Column(name = "id_Store")
    private Long id;

    @Column(name = "Location", nullable = false, length = 100)
    private String location;

    @Column(name = "Responsible", nullable = false, length = 100)
    private String responsible;

    // Constructor vacío (obligatorio para JPA)
    public Store() {}

    // Constructor con parámetros (sin id porque es auto-generado)
    public Store(String location, String responsible) {
        this.location = location;
        this.responsible = responsible;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }
}
