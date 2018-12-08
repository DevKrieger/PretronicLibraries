package net.prematic.libraries.sql.query;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 05.12.18 18:41
 *
 */

import net.prematic.libraries.sql.SQL;
import net.prematic.libraries.tasking.TaskOwner;
import net.prematic.libraries.tasking.intern.PrematicTask;
import net.prematic.libraries.tasking.intern.PrematicTaskScheduler;
import net.prematic.libraries.tasking.intern.SystemTaskOwner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class ExecuteQuery extends Query {

    public ExecuteQuery(SQL sql, String query) {
        super(sql, query);
    }

    public void executeAsync() {
        sql.getScheduler().runTaskAsync(new SystemTaskOwner(), this::execute);
    }

    public void executeAsync(int autoGeneratedKeys, Consumer<Object> consumer) {
        sql.getScheduler().runTaskAsync(new SystemTaskOwner(), ()-> consumer.accept(this.execute(autoGeneratedKeys)));
    }

    public void execute() {
        execute(0);
    }

    public Object execute(int autoGeneratedKeys) {
        this.endOptions.forEach((field, option)-> {
            this.query = query.substring(0,query.length()-1)+",";
            this.query += option+" ("+field+"))";
        });
        try(final PreparedStatement preparedStatement = getConnection().prepareStatement(query, autoGeneratedKeys)) {
            int i = 1;
            for (Object object : values) {
                preparedStatement.setString(i, object.toString());
                i++;
            }
            preparedStatement.executeUpdate();
            if(autoGeneratedKeys != 0) {
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