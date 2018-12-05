package net.prematic.libraries.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Copyright (c) 2018 Philipp Elvin Friedhoff on 04.12.1018 21:18
 */

public class MySQL extends SQL {

    private DataSource dataSource;
    private String host, port, database, user, password;
    private List<String> connectionFlags;
    public static List<String> DEFAULT_CONNECTION_FLAGS;

    static {
        DEFAULT_CONNECTION_FLAGS = new ArrayList<>();
        DEFAULT_CONNECTION_FLAGS.add("autoReconnect=true");
        DEFAULT_CONNECTION_FLAGS.add("allowMultiQueries=true");
        DEFAULT_CONNECTION_FLAGS.add("reconnectAtTxEnd=true");
    }

    public MySQL(String host, String port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.connectionFlags = new ArrayList<>();
    }

    public MySQL(String host, int port, String database, String user, String password) {
        this(host, String.valueOf(port), database, user, password);
    }

    public List<String> getConnectionFlags() {
        return connectionFlags;
    }

    public String buildConnectionFlags() {
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        Iterator<String> defaultConnectionFlagsIterator = DEFAULT_CONNECTION_FLAGS.iterator();
        while (defaultConnectionFlagsIterator.hasNext()) {
            builder.append(defaultConnectionFlagsIterator.next());
            if(defaultConnectionFlagsIterator.hasNext() || !this.connectionFlags.isEmpty()) builder.append("&");
        }
        Iterator<String> iterator = this.connectionFlags.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if(iterator.hasNext()) builder.append("&");
        }
        return builder.toString();
    }

    @Override
	public boolean connect() {
        loadDriver();
        getDataSource();
        return true;
	}

    @Override
    public void disconnect() {

    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DataSource getDataSource() {
        if(this.dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://"+this.host+":"+this.port+"/"+this.database);
            config.setUsername(this.user);
            config.setPassword(this.password);
            config.setMaximumPoolSize(10);
            //config.setAutoCommit(false);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            this.dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }

    @Override
	public void loadDriver(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			System.out.println("Could not load MySQL driver");
		}
	}
}
