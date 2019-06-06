package com.dawid.overtimevaadin.dto;

import java.time.Duration;
import java.util.Set;

public class Statistic {

    private Long id;
    private Duration balance;
    private Set<Overtime> overtime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Duration getBalance() {
        return balance;
    }

    public String getFormattedBalance() {
        return balance.toString().substring(2);
    }

    public void setBalance(Duration balance) {
        this.balance = balance;
    }

    public Set<Overtime> getOvertime() {
        return overtime;
    }

    public void setOvertime(Set<Overtime> overtime) {
        this.overtime = overtime;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "id=" + id +
                ", balance=" + balance +
                ", overtime=" + overtime +
                '}';
    }
}
