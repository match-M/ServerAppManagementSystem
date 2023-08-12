package com.match.exception;

public class ConfigValueNotFoundException extends Exception{
    public ConfigValueNotFoundException(String errorInfo){
        super(errorInfo);
    }
}
