package net.prematic.libraries.sql.query;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 05.12.18 18:41
 *
 */

import net.prematic.libraries.sql.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecuteQuery extends Query {

    public ExecuteQuery(SQL sql, String query) {
        super(sql, query);
    }

    public void execute() {
        execute(0);
    }

    public Object execute(int autGeneratedKeys) {
        System.out.println(query);

        this.endOptions.forEach((field, option)-> {
            this.query = query.substring(0,query.length()-1)+",";

            this.query += option+" ("+field+"))";
        });
        System.out.println(query);

        try(final PreparedStatement preparedStatement = getConnection().prepareStatement(query, autGeneratedKeys)) {
            int i = 1;
            for (Object object : values) {
                preparedStatement.setString(i, object.toString());
                i++;
            }
            preparedStatement.executeUpdate();
            if(autGeneratedKeys != 0) {
                ResultSet result = preparedStatement.getGeneratedKeys();
                if(result != null){
                    if(result.next()) return result.getObject(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}