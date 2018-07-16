package com.mysql.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;

public class Main {

    public static void main( String[] args ) {

        Connection conn = new ConnectionFactory().getConnection();

        DAO DAO = new DAO(conn);
        DAO.initDB();
        
        DAO.selectByPrice(400000, 25000000);
        DAO.selectByArea(BigDecimal.valueOf(50), BigDecimal.valueOf(90));
        DAO.selectByRoomCount(2, 3);
        DAO.selectByTown("Kiev");
        DAO.selectByDistrict("Obolonskiy");

    }
}
