package com.mysql.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

public class DAO implements DAOInterface{

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPATest");
    private static final EntityManager em = emf.createEntityManager();

    private static User user;

    public static void fillDB(){
        createUsers();
        initExchangeRate();
    }

    @Override
    public boolean signUp(String name, String email, String phoneNumber, String password){
        em.getTransaction().begin();
        user = new User(name, email, phoneNumber, password);
        em.persist(user);
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean signIn(String name, String password){
        Query query = em.createQuery("select u from User u where name = :name and password = :password");
        query.setParameter("name", name);
        query.setParameter("password", password);
        user = (User) query.getSingleResult();
        return user != null;
    }

    @Override
    public List getAccounts(){
        Query query = em.createQuery("select a from Account a");
        return query.getResultList();
    }

    @Override
    public List getTransactions(){
        Query query = em.createQuery("select t from Transaction t");
        return query.getResultList();
    }

    @Override
    public BigDecimal getUserMoneyFromAllAccounts(String currency){

        BigDecimal money = BigDecimal.ZERO;

        ExchangeRate exchangeRate;
        BigDecimal factor;
        for (Account account : user.getAccounts()) {
            exchangeRate = getExchangeRate(account.getCurrency());
            factor = getCurrencyFactor(currency, exchangeRate);
            money = money.add(account.getMoney().multiply(factor));
        }
        return money;
    }

    @Override
    public void addAccount(String currency){
        em.getTransaction().begin();
        Account account = new Account(user, BigDecimal.ZERO, currency);
        user.addAccount(account);
        em.merge(user);
        em.getTransaction().commit();
    }

    @Override
    public void changeAccountCurrency(long userId, String currency){
        em.getTransaction().begin();
        Account account = getAccountById(userId);

        String accountCurrency = account.getCurrency();
        BigDecimal accountMoney = account.getMoney();
        ExchangeRate exchangeRate = getExchangeRate(accountCurrency);

        BigDecimal factor = getCurrencyFactor(currency, exchangeRate);
        accountMoney = accountMoney.multiply(factor);
        account.setMoney(accountMoney);
        account.setCurrency(currency);

        em.merge(account);
        em.getTransaction().commit();
    }

    @Override
    public void replenish(long userId, BigDecimal money, String currency){
        em.getTransaction().begin();
        Account account = getAccountById(userId);

        Transaction transaction = new Transaction(account, account, money, currency);
        ExchangeRate exchangeRate = getExchangeRate(currency);
        performReceive(account, money, exchangeRate);

        em.persist(transaction);
        em.getTransaction().commit();
    }

    @Override
    public void performTransaction(long senderId, long receiverId, BigDecimal money, String currency){
        em.getTransaction().begin();
        Account senderAccount = getAccountById(senderId);
        Account receiverAccount = getAccountById(receiverId);

        Transaction transaction = new Transaction(senderAccount, receiverAccount, money, currency);

        ExchangeRate exchangeRate = getExchangeRate(currency);

        performSend(senderAccount, money, exchangeRate);
        performReceive(receiverAccount, money, exchangeRate);

        em.persist(transaction);
        em.getTransaction().commit();

    }

    private Account getAccountById(long id){
        Query query = em.createQuery("select a from Account a where id = :id");
        query.setParameter("id", id);
        return (Account) query.getSingleResult();
    }

    private void performSend(Account senderAccount, BigDecimal money, ExchangeRate rate){
        String senderCurrency = senderAccount.getCurrency();
        BigDecimal senderMoney = senderAccount.getMoney();

        money = money.multiply(getCurrencyFactor(senderCurrency, rate));
        senderAccount.setMoney(senderMoney.add(money.negate()));
    }

    private void performReceive(Account receiverAccount, BigDecimal money, ExchangeRate rate){
        String receiverCurrency = receiverAccount.getCurrency();
        BigDecimal receiverMoney = receiverAccount.getMoney();

        money = money.multiply(getCurrencyFactor(receiverCurrency, rate));
        receiverAccount.setMoney(receiverMoney.add(money));
    }

    private ExchangeRate getExchangeRate(String currency){
        Query query = em.createQuery("select e from ExchangeRate e where currency = :currency");
        query.setParameter("currency", currency);
        return (ExchangeRate) query.getSingleResult();
    }

    private BigDecimal getCurrencyFactor(String currency, ExchangeRate rate){

        if (currency.equals("USD")){
            return rate.getUSD();
        }
        else if (currency.equals("EUR")){
            return rate.getEUR();
        }
        else if (currency.equals("UAH")){
            return rate.getUAH();
        }
        return BigDecimal.ZERO;
    }

    private static void createUsers(){
        em.getTransaction().begin();
        User user1 = new User("Emily", "emily25@gmail.com", "+380509937584", "123");
        Account a1 = new Account(user1, BigDecimal.valueOf(2935), "UAH");
        Account a2 = new Account(user1, BigDecimal.valueOf(50.8), "EUR");
        user1.addAccount(a1);
        user1.addAccount(a2);
        em.persist(user1);

        User user2 = new User("Jack", "jack62@ukr.net", "+380974638264", "111");
        Account a3 = new Account(user2, BigDecimal.valueOf(356.2), "USD");
        user2.addAccount(a3);
        em.persist(user2);

        em.getTransaction().commit();
    }

    private static void initExchangeRate(){
        em.getTransaction().begin();
        ExchangeRate USD = new ExchangeRate("USD", BigDecimal.ONE, BigDecimal.valueOf(0.85), BigDecimal.valueOf(26.47));
        ExchangeRate EUR = new ExchangeRate("EUR", BigDecimal.valueOf(1.17), BigDecimal.ONE, BigDecimal.valueOf(30.68));
        ExchangeRate UAH = new ExchangeRate("UAH", BigDecimal.valueOf(0.03), BigDecimal.valueOf(0.03), BigDecimal.ONE);
        em.persist(USD);
        em.persist(EUR);
        em.persist(UAH);
        em.getTransaction().commit();
    }

}
