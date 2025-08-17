package pe.edu.vallegrande.project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Supplier", nullable = false)
    private Long idSupplier;

    @Column(name = "Document_Type", nullable = false, length = 3)
    private String documentType;

    @Column(name = "Document_Number", nullable = false, unique = true, length = 50)
    private String documentNumber;

    @Column(name = "Name", nullable = false, length = 50)
    private String name;

    @Column(name = "Surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "Address", length = 100)
    private String address;

    @Column(name = "Email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "Phone", length = 100)
    private String phone;

    @Column(name = "Status", nullable = false, length = 1)
    private String status = "A";
}
