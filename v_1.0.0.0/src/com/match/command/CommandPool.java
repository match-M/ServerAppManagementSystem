package com.match.command;

import java.util.HashSet;
import java.util.Set;

public class CommandPool {
    public static final Set<String> logCommand = new HashSet<>();
    public static final Set<String> configCommand = new HashSet<>();

    static {
        logCommand.add("show");
        logCommand.add("seek");
        configCommand.add("put");
        configCommand.add("load");
        configCommand.add("remove");
    }
}
