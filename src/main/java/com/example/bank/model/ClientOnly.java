package com.example.bank.model;

public class ClientOnly {
    private Long clientId;
    private String name;
    private String shortName;
    private String address;
    private Clients.OrganizationalForm form;

    public ClientOnly(Long clientId, String name, String shortName, String address, Clients.OrganizationalForm form) {
        this.clientId = clientId;
        this.name = name;
        this.shortName = shortName;
        this.address = address;
        this.form = form;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Clients.OrganizationalForm getForm() {
        return form;
    }

    public void setForm(Clients.OrganizationalForm form) {
        this.form = form;
    }
}