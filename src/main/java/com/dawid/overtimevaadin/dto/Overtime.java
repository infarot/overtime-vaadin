package com.dawid.overtimevaadin.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Duration;
import java.time.LocalDate;

public class Overtime implements Comparable<Overtime> {

    private Long id;
    private Duration amount;
    private LocalDate overtimeDate;
    private LocalDate pickUpDate;
    private String remarks;
    private String stringAmount;

    @JsonIgnore
    public String getStringAmount() {
        return stringAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStringAmount(String stringAmount) {
        this.stringAmount = stringAmount;
    }

    public Duration getAmount() {
        return amount;
    }

    public String getFormattedAmount() {
        return amount.toString().substring(2);
    }

    public void setAmount(Duration amount) {
        this.amount = amount;
    }

    public LocalDate getOvertimeDate() {
        return overtimeDate;
    }

    public void setOvertimeDate(LocalDate overtimeDate) {
        this.overtimeDate = overtimeDate;
    }

    public LocalDate getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(LocalDate pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    @Override
    public String toString() {
        return "Overtime{" +
                "amount=" + amount +
                ", overtimeDate=" + overtimeDate +
                ", pickUpDate=" + pickUpDate +
                ", remarks='" + remarks + '\'' +
                ", stringAmount='" + stringAmount + '\'' +
                '}';
    }

    @Override
    public int compareTo(Overtime o) {
        return o.getOvertimeDate().compareTo(getOvertimeDate());
    }
}
