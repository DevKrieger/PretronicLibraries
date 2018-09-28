package net.prematic.libraries.sql.query;

import net.prematic.libraries.sql.SQL;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

/*
 * Copyright (c) 2018 Dkrieger on 16.05.18 15:49
 */

public class Query {

    protected Connection connection;
    protected String query;
    protected boolean firstvalue;
    protected boolean and;
    protected boolean comma;
    protected List<Object> values;

    public Query(Connection connection, String query){
        this.connection = connection;
        this.query = query;
        this.firstvalue = true;
        this.comma = false;
        this.and = false;
        this.values = new LinkedList<>();
    }

    public Connection getConnection() {
        return connection;
    }

    public List<Object> getValues(){
        return this.values;
    }
    public String toString(){
        return this.query;
    }
}
