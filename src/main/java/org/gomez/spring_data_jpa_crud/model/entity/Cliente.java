package org.gomez.spring_data_jpa_crud.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Atributo para la ruta de la foto del cliente en el sistema de archivos
    private String foto;

    // Atributos obligatorios para el cliente: nombre, apellido y correo electrónico
    @NotEmpty
    private String nombre;

    @NotEmpty
    private String apellido;

    @Email
    @NotEmpty
    private String email;

    // Atributo para la fecha de creación del cliente
    @NotNull
    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;

    // Constructor vacío necesario para JPA
    public Cliente() {
    }

    // Constructor con parámetros para facilitar la creación de instancias
    public Cliente(Long id, String foto, String nombre, String apellido, String email, @NotNull Date createAt) {
        this.id = id;
        this.foto = foto;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.createAt = createAt;
    }

    // Métodos getters y setters para acceder y modificar los atributos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
