package net.prematic.libraries.sql.table;

import net.prematic.libraries.sql.SQL;
import net.prematic.libraries.sql.*;
import net.prematic.libraries.sql.query.*;

/*
 * Copyright (c) 2018 Dkrieger on 16.05.18 15:49
 * Copyright (c) 2018 Philipp Elvin Friedhoff 05.12.18 14:40
 */

public class Table {

    private String name;
    private SQL sql;

    public Table(SQL sql,String name) {
        this.name = name;
        this.sql = sql;
    }

    public String getName(){
        return this.name;
    }

    public SQL getSQL() {
        return this.sql;
    }

    public CreateQuery create(){
        return new CreateQuery(sql.getConnection(),"CREATE TABLE IF NOT EXISTS `"+this.name+"` (");
    }

    public InsertQuery insert(){
        return new InsertQuery(sql.getConnection(),"INSERT INTO `"+this.name+"` (");
    }

    public UpdateQuery update(){
        return new UpdateQuery(sql.getConnection(),"UPDATE `"+this.name+"` SET");
    }

    public SelectQuery selectAll(){
        return select("*");
    }

    public SelectQuery select() {
        return selectAll();
    }

    public SelectQuery select(String selection){
        return new SelectQuery(sql.getConnection(), "SELECT "+selection+" FROM `"+this.name+"`");
    }

    public DeleteQuery delete(){
        return new DeleteQuery(sql.getConnection(), "DELETE FROM `"+this.name+"`");
    }
}
