package com.example.bank;

import java.util.Date;

public class User {
    private final Integer id;
    private final String name;
    private final String secondname;
    private final String address;
    private final int postIndex;
    private final Date regDate;
    private final String balance;

    public User(Integer id, String name, String secondname, String address, int postIndex, Date date, String balance) {
        this.id = id;
        this.name = name;
        this.secondname = secondname;
        this.address = address;
        this.postIndex = postIndex;
        this.regDate = date;
        this.balance = balance;
    }
    public Date getDate() {
        return regDate;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSecondname() {
        return secondname;
    }

    public String getAddress() {
        return address;
    }

    public int getPostIndex() {
        return postIndex;
    }

    public String getBalance() {
        return balance;
    }
}
