package net.prematic.libraries.sql.query;

import net.prematic.libraries.sql.SQL;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/*
 * Copyright (c) 2018 Dkrieger on 16.05.18 15:49
 * Copyright (c) 2018 Philipp Elvin Friedhoff 05.12.18 14:25
 */

public class CreateQuery extends ExecuteQuery {





    public CreateQuery(SQL sql, String query){
        super(sql, query);
        firstvalue = true;
    }

    public CreateQuery create(String field, String type, String... options) {
        return create(field, type, 0, options);
    }

    public CreateQuery create(String field, String type, int size, String... options) {
        StringBuilder builder = new StringBuilder();
        builder.append("`").append(field).append("` ").append(type);
        if(size != 0) builder.append("(").append(size).append(")");
        for(String option : options) {
            if(this.sql.isOptionsOnEnd() && DEFAULT_END_OPTIONS.contains(option.toUpperCase())) this.endOptions.put(field, option);
            else builder.append(" ").append(option);
        }
        return create(builder.toString());
    }

    public CreateQuery create(String value) {
        if(!firstvalue) query = query.substring(0,query.length()-1)+",";
        else firstvalue = false;
        query += value+")";
        return this;
    }
}
