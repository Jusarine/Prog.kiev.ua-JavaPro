package com.mysql.jpa;

import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal money;

    private String currency;

    @OneToMany(mappedBy = "senderAccount", cascade = CascadeType.ALL)
    private List<Transaction> sentTransfers = new LinkedList<>();

    @OneToMany(mappedBy = "receiverAccount", cascade = CascadeType.ALL)
    private List<Transaction> receivedTransfers = new LinkedList<>();

    public Account() {
    }

    public Account(User user, BigDecimal money, String currency) {
        this.user = user;
        this.money = money;
        this.currency = currency;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<Transaction> getSentTransfers() {
        return sentTransfers;
    }

    public List<Transaction> getReceivedTransfers() {
        return receivedTransfers;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", user=" + user +
                ", money=" + money +
                ", currency='" + currency + '\'' +
                '}';
    }
}
