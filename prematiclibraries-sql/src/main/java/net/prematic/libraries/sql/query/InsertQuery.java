package net.prematic.libraries.sql.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Copyright (c) 2018 Dkrieger on 16.05.18 15:49
 * Copyright (c) 2018 Philipp Elvin Friedhoff 05.12.18 14:32
 */

public class InsertQuery extends ExecuteQuery {

    public InsertQuery(Connection connection, String query) {
        super(connection, query);
    }

    public InsertQuery insert(String insert) {
        query += "`"+insert+"`,";
        return this;
    }

    public InsertQuery value(Object value) {
        query = query.substring(0, query.length() - 1);
        if(firstvalue){
            query += ") VALUES (?)";
            firstvalue = false;
        }else query += ",?)";
        values.add(value);
        return this;
    }

    public Object executeAndGetKey() {
        try {
            preparedStatement = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            for(Object object : values) {
                preparedStatement.setString(i, object.toString());
                i++;
            }
            preparedStatement.executeUpdate();
            ResultSet result = preparedStatement.getGeneratedKeys();
            if(result != null){
                if(result.next()) return result.getObject(1);
            }
            if(result != null) result.close();
            preparedStatement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int executeAndGetKeyAsInt(){
        try {
            preparedStatement = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            for(Object object : values) {
                preparedStatement.setString(i, object.toString());
                i++;
            }
            preparedStatement.executeUpdate();
            ResultSet result = preparedStatement.getGeneratedKeys();
            if(result != null){
                if(result.next()) return result.getInt(1);
            }
            if(result != null) result.close();
            preparedStatement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
