package org.gomez.spring_data_jpa_crud.model.dao;

import org.gomez.spring_data_jpa_crud.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

// Interfaz que extiende JpaRepository para operaciones CRUD y de paginación
public interface IClienteDao extends JpaRepository<Cliente, Long> {
    // Esta interfaz hereda métodos básicos de JpaRepository, como save, findById, findAll, delete, etc.
    // No es necesario añadir nuevos métodos, ya que JpaRepository proporciona métodos predefinidos para operaciones comunes en la base de datos.
}
