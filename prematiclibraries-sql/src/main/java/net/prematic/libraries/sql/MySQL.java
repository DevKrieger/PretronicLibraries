package net.prematic.libraries.sql;

import net.prematic.libraries.tasking.intern.PrematicTaskScheduler;
import net.prematic.libraries.tasking.intern.SystemTaskOwner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/*
 * Copyright (c) 2018 Dkrieger on 16.05.18 15:49
 */

public class MySQL {

	private String systemname;
	private String host;
	private String port;
	private String user;
	private String password;
	private String database;
	private Connection conn;

	public MySQL(String systemname, String host, int port, String user, String password, String database){
		this(systemname,host,String.valueOf(port),user,password,database);
	}
	public MySQL(String systemname, String host, String port, String user, String password, String database){
		this.systemname = systemname;
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.database = database;
		new PrematicTaskScheduler().schedule(new SystemTaskOwner(), new Runnable() {
			@Override
			public void run() {
				reconnect();
			}
		}, 0L, 2L, TimeUnit.HOURS);
	}
	public void loadDriver(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			System.out.println("Could not load MySQL driver");
		}
	}
	public Boolean connect(){
		if(!isConnect()){
			loadDriver();
			System.out.println("connecting to MySQL server at "+this.host+":"+port);
			try {
				conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database+"?autoReconnect=true&allowMultiQueries=true&reconnectAtTxEnd=true",user, password);
				System.out.println("successful connected to MySQL server at "+this.host+":"+port);
				return true;
			}catch (SQLException e) {
				System.out.println("Could not connect to MySQL server at "+this.host+":"+port);
				e.printStackTrace();
				System.out.println("Could not connect to MySQL server at "+this.host+":"+port);
				conn = null;
			}
		}
		return false;
	}
	public void disconect(){
		if(isConnect()){
			try {
				conn.close();
				conn = null;
				System.out.println("successful disconnected from MySQL server at "+this.host+":"+port);
			}catch (SQLException e) {
				conn = null;
			}
		}
	}
	public void reconnect(){
		disconect();
		connect();
	}
	public boolean isConnect(){
		if(conn == null) return false; else return true;
	}
	public Connection getConnecion(){
		return conn;
	}
	public String getSystemName(){
		return this.systemname;
	}


}
