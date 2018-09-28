package net.prematic.libraries.sql.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Copyright (c) 2018 Dkrieger on 16.05.18 15:49
 */

public class CreateQuery extends Query {

    public CreateQuery(Connection connection, String query){
        super(connection, query);
        firstvalue = true;
    }
    public CreateQuery create(String schema, int size,String... opions) {
        return null;
    }
    public CreateQuery create(String value) {
        if(!firstvalue) query = query.substring(0,query.length()-1)+",";
        else firstvalue = false;
        query += value+")";
        return this;
    }
    public void execute(){
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute(query);
            statement.close();
        } catch(SQLException exception){
            exception.printStackTrace();
        }
    }
}
