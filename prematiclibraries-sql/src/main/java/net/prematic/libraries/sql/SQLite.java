package net.prematic.libraries.sql;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 04.12.18 20:58
 *
 */

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends SQL {

    private File location;
    private Connection connection;

    public SQLite(File location) {
        this.location = location;
    }

    public SQLite(String path, String child) {
        this.location = new File(path, child);
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean connect() {
        try {
            this.location.mkdirs();
            this.location.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            loadDriver();
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.location.getPath());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void disconnect() {
        try {
            this.connection.close();
            this.connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return this.connection != null;
    }
}