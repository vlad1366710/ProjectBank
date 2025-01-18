package com.example.bank.repository;

import com.example.bank.model.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientRepository extends JpaRepository<Clients, Long>, JpaSpecificationExecutor<Clients> {

}
