package com.mysql.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Table {

    public static StringBuilder getTable(ResultSet result) throws SQLException {
        StringBuilder sb = new StringBuilder("<table>");

        ResultSetMetaData md = result.getMetaData();
        sb.append("<tr>");
        for (int i = 1; i <= md.getColumnCount(); i++) {
            sb.append("<th>").append(md.getColumnLabel(i)).append("</th>");
        }
        sb.append("</tr>");

        while (result.next()) {
            sb.append("<tr>");
            for (int i = 1; i <= md.getColumnCount(); i++) {
                sb.append("<td>").append(result.getString(i)).append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");

        return sb;
    }
}
