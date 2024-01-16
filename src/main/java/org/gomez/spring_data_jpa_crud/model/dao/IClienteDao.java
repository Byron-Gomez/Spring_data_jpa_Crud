package org.gomez.spring_data_jpa_crud.model.dao;

import org.gomez.spring_data_jpa_crud.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface IClienteDao extends JpaRepository<Cliente, Long> {
}
