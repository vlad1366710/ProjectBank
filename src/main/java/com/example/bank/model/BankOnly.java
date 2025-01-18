package com.example.bank.model;

public class BankOnly {

    private Long bank_id;

    private String name;

    private String bik;

    public BankOnly(Long bank_id, String name, String bik) {
        this.bank_id = bank_id;
        this.name = name;
        this.bik = bik;
    }

    public Long getBank_id() {
        return bank_id;
    }

    public void setBank_id(Long bank_id) {
        this.bank_id = bank_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBik() {
        return bik;
    }

    public void setBik(String bik) {
        this.bik = bik;
    }
}
