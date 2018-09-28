package net.prematic.libraries.utility.exceptions;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 04.09.18 18:38
 *
 */

public class LimitedListReachedLimitException extends RuntimeException{

    public LimitedListReachedLimitException(int maxvalue) {
        this("Reached limit of "+maxvalue);
    }

    public LimitedListReachedLimitException(String message) {
        super(message);
    }
}