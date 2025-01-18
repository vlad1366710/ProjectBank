package com.example.bank.model;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "deposits")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deposit_id;


    @Column(nullable = false)
    private Long clientId;
    @Column(nullable = false)
    private Long bankId;

    @ManyToOne
    @JoinColumn(name = "clientId", referencedColumnName = "client_id",updatable = false,insertable = false)
    private Clients client;

    // Связь с банком
    @ManyToOne
    @JoinColumn(name = "bankId", referencedColumnName = "bank_id",insertable = false, updatable = false)
    private Bank bank;


    @Column(nullable = false)
    private Date openingDate;

    @Column(nullable = false)
    private Float percent;

    @Column(nullable = false)
    private Integer termMonths;
    public void setClient(Clients client) {
        this.client = client;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Bank getBank() {
        return bank;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Long getId() {
        return deposit_id;
    }

    public void setId(Long id) {
        this.deposit_id = id;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public Integer getTermInMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }
}