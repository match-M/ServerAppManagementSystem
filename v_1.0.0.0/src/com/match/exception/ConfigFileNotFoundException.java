package com.match.exception;

public class ConfigFileNotFoundException extends Exception{
    public ConfigFileNotFoundException(String errorInfo){
        super(errorInfo);
    }
}
