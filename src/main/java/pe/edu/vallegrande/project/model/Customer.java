package pe.edu.vallegrande.project.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Customer")
    private Long idCustomer;

    @Column(name = "ClientCode", nullable = false, length = 15, unique = true)
    private String clientCode;  // Nuevo campo ClientCode, longitud ajustada y único

    @Column(name = "Document_Type", nullable = false, length = 3)
    private String documentType;

    @Column(name = "Document_Number", nullable = false, length = 20)
    private String documentNumber;

    @Column(name = "Name", nullable = false, length = 50)
    private String name;

    @Column(name = "Surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "Address", length = 100)
    private String address;

    @Column(name = "Phone", length = 9)
    private String phone;  // ✅ CAMBIADO a String

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Date_Birth")
    private LocalDate dateBirth;

    @Column(name = "Status", nullable = false, length = 1)
    private String status = "A";

    @Column(name = "Role", length = 20)
    private String role;

    // Aquí puedes agregar el getter y setter si no estás usando Lombok para el clientCode
}
