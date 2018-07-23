package com.mysql.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "exchange_rates")
public class ExchangeRate {

    @Id
    @GeneratedValue
    private long id;

    private String currency;

    private BigDecimal USD;

    private BigDecimal EUR;

    private BigDecimal UAH;

    public ExchangeRate() {
    }

    public ExchangeRate(String currency, BigDecimal USD, BigDecimal EUR, BigDecimal UAH) {
        this.currency = currency;
        this.USD = USD;
        this.EUR = EUR;
        this.UAH = UAH;
    }

    public long getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getUSD() {
        return USD;
    }

    public void setUSD(BigDecimal USD) {
        this.USD = USD;
    }

    public BigDecimal getEUR() {
        return EUR;
    }

    public void setEUR(BigDecimal EUR) {
        this.EUR = EUR;
    }

    public BigDecimal getUAH() {
        return UAH;
    }

    public void setUAH(BigDecimal UAH) {
        this.UAH = UAH;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", currency='" + currency + '\'' +
                ", USD=" + USD +
                ", EUR=" + EUR +
                ", UAH=" + UAH +
                '}';
    }
}
