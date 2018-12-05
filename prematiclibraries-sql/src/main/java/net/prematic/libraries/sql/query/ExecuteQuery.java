package net.prematic.libraries.sql.query;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 05.12.18 18:41
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecuteQuery extends Query {

    public ExecuteQuery(Connection connection, String query) {
        super(connection, query);
    }

    public void execute() {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int i = 1;
            for (Object object : values) {
                preparedStatement.setString(i, object.toString());
                i++;
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}