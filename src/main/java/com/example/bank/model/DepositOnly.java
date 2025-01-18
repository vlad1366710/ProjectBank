package com.example.bank.model;

import java.util.Date;

public class DepositOnly {
    private Long deposit_id;

    private Date openingDate;

    private Float percent;

    private Integer termMonths;

    public DepositOnly(Long deposit_id, Date openingDate, Float percent, Integer termMonths) {
        this.deposit_id = deposit_id;

        this.openingDate = openingDate;
        this.percent = percent;
        this.termMonths = termMonths;
    }

    public Long getDeposit_id() {
        return deposit_id;
    }

    public void setDeposit_id(Long deposit_id) {
        this.deposit_id = deposit_id;
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

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }
}
