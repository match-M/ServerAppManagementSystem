package com.match.command;

import com.match.tools.LogTools;

import java.util.HashSet;
import java.util.Set;

public class ParameterPool {
    public static final Set<String> logParameter = new HashSet<>();
    public static final Set<String> configParameter  = new HashSet<>();

    static {
        logParameter.add("-3");
        logParameter.add("-5");
        logParameter.add("-6");
        logParameter.add("-list");
        logParameter.add("-name");
        logParameter.add("-load");
        configParameter.add("-list");
        configParameter.add("-name");
        configParameter.add("-value");

    }


}
