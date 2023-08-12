package com.match.command;

import com.match.constants.CommandExceptionConst;

public class CommandErrorPrompt {
    public CommandErrorPrompt(String errorCmd){
        System.err.println(errorCmd+" "+ CommandExceptionConst.COMMAND_NOTFOUND);
    }

    public CommandErrorPrompt(String errorCmd, String errorInfo){
        System.err.println(errorCmd+" "+ errorInfo);
    }
}
