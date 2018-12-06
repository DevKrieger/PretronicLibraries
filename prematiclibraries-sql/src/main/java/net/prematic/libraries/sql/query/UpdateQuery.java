package net.prematic.libraries.sql.query;

import net.prematic.libraries.sql.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * Copyright (c) 2018 Dkrieger on 16.05.18 15:49
 * Copyright (c) 2018 Philipp Elvin Friedhoff 05.12.18 14:57
 */

public class UpdateQuery extends ExecuteQuery {

    public UpdateQuery(SQL sql, String query) {
        super(sql, query);
    }
    public UpdateQuery set(String field, Object value) {
        if (comma) query += ",";
        query += " `"+field+"`=?";
        values.add(value);
        comma = true;
        return this;
    }
    public UpdateQuery where(String key, Object value) {
        if(and) query += " AND";
        else query += " WHERE";
        query +=" "+key+"=?";
        values.add(value);
        and = true;
        return this;
    }
}