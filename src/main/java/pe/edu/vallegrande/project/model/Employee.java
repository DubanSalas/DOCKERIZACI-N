package pe.edu.vallegrande.project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Employee", nullable = false)
    private Long idEmployee;

    @Column(name = "Name", nullable = false, length = 50)
    private String name;

    @Column(name = "Surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "Post", nullable = false, length = 20)
    private String post;

    @Column(name = "Phone", nullable = true, length = 9)
    private String phone;

    @Column(name = "Status", nullable = false, length = 1)
    private String status = "A";  // Activo por defecto
}
