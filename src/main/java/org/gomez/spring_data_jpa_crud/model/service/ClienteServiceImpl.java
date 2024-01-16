package org.gomez.spring_data_jpa_crud.model.service;

import org.gomez.spring_data_jpa_crud.exception.ResourceNotFoundException;
import org.gomez.spring_data_jpa_crud.model.dao.IClienteDao;
import org.gomez.spring_data_jpa_crud.model.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    // Obtiene todos los clientes
    @Transactional(readOnly = true)
    @Override
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteDao.findAll();
    }

    // Obtiene todos los clientes paginados
    @Transactional(readOnly = true)
    @Override
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }

    // Guarda un nuevo cliente o actualiza uno existente
    @Transactional
    @Override
    public void save(Cliente cliente) {
        clienteDao.save(cliente);
    }

    // Obtiene un cliente por su ID o lanza una excepción si no se encuentra
    @Transactional(readOnly = true)
    @Override
    public Cliente findOne(Long id) {
        return clienteDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona does not exist with id: " + id));
    }

    // Elimina un cliente por su ID o lanza una excepción si no se encuentra
    @Transactional()
    @Override
    public void delete(Long id) {
        Cliente cliente = clienteDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona does not exist with id: " + id));

        clienteDao.delete(cliente);
    }
}
