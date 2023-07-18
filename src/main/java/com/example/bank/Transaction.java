package com.example.bank;

import java.math.BigDecimal;
import java.sql.Date;

public class Transaction {
    private final int idTrans;
    private final String purpose;
    private final BigDecimal sum;
    private final Date date;
    private final BigDecimal balance;

    public Transaction(int idTrans, String purpose, BigDecimal sum, Date date, BigDecimal balance) {
        this.idTrans = idTrans;

        this.purpose = purpose;
        this.sum = sum;
        this.date = date;
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public int getIdTrans() {
        return idTrans;
    }

    public String getPurpose() {
        return purpose;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public Date getDate() {
        return date;
    }
}
