package org.gomez.spring_data_jpa_crud.model.service;

import org.gomez.spring_data_jpa_crud.model.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

// Interfaz que define operaciones de servicio para la entidad Cliente
public interface IClienteService {

    // Obtiene todos los clientes
    List<Cliente> findAll();

    // Obtiene todos los clientes paginados
    Page<Cliente> findAll(Pageable pageable);

    // Guarda un nuevo cliente o actualiza uno existente
    void save(Cliente cliente);

    // Obtiene un cliente por su ID
    Cliente findOne(Long id);

    // Elimina un cliente por su ID
    void delete(Long id);
}
