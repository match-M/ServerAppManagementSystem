package com.match.exception;

public class ConfigNameNotFoundException extends Exception {
    public ConfigNameNotFoundException(String errorInfo){
        super(errorInfo);
    }
}
