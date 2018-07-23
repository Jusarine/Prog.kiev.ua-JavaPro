package com.mysql.jpa;

import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id")
    private Account senderAccount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_id")
    private Account receiverAccount;

    private BigDecimal money;

    private String currency;

    public Transaction() {
    }

    public Transaction(Account senderAccount, Account receiverAccount, BigDecimal money, String currency) {
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.money = money;
        this.currency = currency;
    }

    public long getId() {
        return id;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    public Account getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(Account receiverAccount) {
        this.receiverAccount = receiverAccount;
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", senderAccount=" + senderAccount +
                ", receiverAccount=" + receiverAccount +
                ", money=" + money +
                ", currency='" + currency + '\'' +
                '}';
    }
}
