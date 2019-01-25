package net.prematic.libraries.mongodb;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 09.12.18 16:16
 *
 */

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import net.prematic.libraries.multistorage.Storage;
import net.prematic.libraries.tasking.TaskScheduler;
import net.prematic.libraries.tasking.intern.PrematicTaskScheduler;

public class MongoDB implements Storage {

    private MongoClient mongoClient;
    private TaskScheduler scheduler;
    private boolean ignoreCase;
    private String host, user, password, authenticationDatabase;
    private boolean srv, ssl;

    public MongoDB(String host, String user, String password, boolean srv) {
        this.scheduler = new PrematicTaskScheduler();
        this.ignoreCase = true;
    }

    @Override
    public boolean connect() {
        /*
        String uri = "mongodb"+(config.hasMongoDbSrv()?"+srv":"")+"://";
        if(config.hasMongoDbAuthentication()) uri += config.getUser()+":"+config.getPassword()+"@";
        uri += config.getHost()+"/";
        if(config.hasMongoDbAuthentication()) uri += config.getMongoDbAuthenticationDatabase();
        uri += "?retryWrites=true&connectTimeoutMS=500&socketTimeoutMS=500";
        //(login.hasSSL()) uri+= "&ssl=true";

        this.mongoClient = new MongoClient(new MongoClientURI(uri));
        this.database = this.mongoClient.getDatabase(config.getDatabase());
        this.friendPlayerCollection = database.getCollection("DKFriends_players");
         */
        String url = "mongodb" + (this.user != null && this.password != null ? this.user + ":" + this.password + "@" : "")
                + this.host + "/" + (this.authenticationDatabase != null ? this.authenticationDatabase : "")
                + "?retryWrites=true&connectTimeoutMS=500&socketTimeoutMS=500" + (this.ssl ? "&ssl=true" : "");
        this.mongoClient = new MongoClient(new MongoClientURI(url));
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
    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    @Override
    public TaskScheduler getScheduler() {
        return scheduler;
    }

    public MongoDatabase getDatabase(String database) {
        return this.mongoClient.getDatabase(database);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}