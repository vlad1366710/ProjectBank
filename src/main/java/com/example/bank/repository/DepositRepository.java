package com.example.bank.repository;

import com.example.bank.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long>, JpaSpecificationExecutor<Deposit> {


}
