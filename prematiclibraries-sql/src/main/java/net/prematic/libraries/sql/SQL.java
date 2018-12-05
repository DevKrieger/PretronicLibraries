package net.prematic.libraries.sql;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 04.12.1018 21:10
 *
 */

import net.prematic.libraries.multistorage.Storage;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class SQL implements Storage {


    public abstract Connection getConnection();
    public abstract void loadDriver();
}
