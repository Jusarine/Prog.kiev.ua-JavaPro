package com.mysql.jdbc;

import java.math.BigDecimal;

public interface DAOInterface {

    void initDB();

    void selectByPrice(int from, int to);
    void selectByRoomCount(int from, int to);
    void selectByArea(BigDecimal from, BigDecimal to);
    void selectByTown(String town);
    void selectByDistrict(String district);
}
