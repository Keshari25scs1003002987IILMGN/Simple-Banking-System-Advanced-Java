package com.cyrostack.bank.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final String id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    private final TransactionType type;
    private final double amount;
    private final double balanceAfter;
    private final LocalDateTime time = LocalDateTime.now();

    public Transaction(TransactionType type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return String.format("%s | %s | %-13s | Rs. %,12.2f | Bal: Rs. %,12.2f",
                id, time.format(FMT), type, amount, balanceAfter);
    }
}
