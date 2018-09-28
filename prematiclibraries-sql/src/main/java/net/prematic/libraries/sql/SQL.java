package net.prematic.libraries.sql;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 04.09.18 18:49
 *
 */

import net.prematic.libraries.sql.query.Query;

import java.sql.Connection;

public interface SQL {

    public Connection getConnection();

    public void loadDriver();

    public Boolean connect();

    public Boolean disconnect();

    public Boolean reconnect();

    public Boolean isConnected();

    public void execute(Query query);

    public void execute(String query);

}
