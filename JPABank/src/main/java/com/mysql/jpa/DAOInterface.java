package com.mysql.jpa;

import java.math.BigDecimal;
import java.util.List;

public interface DAOInterface {

    boolean signUp(String name, String email, String phoneNumber, String password);
    boolean signIn(String name, String password);

    List getAccounts();
    List getTransactions();

    BigDecimal getUserMoneyFromAllAccounts(String currency);

    void addAccount(String currency);

    void changeAccountCurrency(long userId, String currency);

    void replenish(long userId, BigDecimal money, String currency);

    void performTransaction(long senderId, long receiverId, BigDecimal money, String currency);
}
