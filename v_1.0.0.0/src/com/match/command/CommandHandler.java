package com.match.command;

import com.match.config.ConfigList;
import com.match.constants.*;
import com.match.exception.ConfigFileNotFoundException;
import com.match.exception.ConfigValueNotFoundException;
import com.match.exception.LogFileNotFoundException;
import com.match.exception.LogListNullException;
import com.match.log.LOGLoader;
import com.match.tools.ConfigTools;
import com.match.tools.LogTools;

import java.io.PrintWriter;
import java.util.Set;


public class CommandHandler {
    private static String cmd;
    private static PrintWriter out;
    private static LogTools logTools;
    private static int logLoadNumber = 0;
    private static ConfigList configList;
    private static ConfigTools configTools;
    private static int openLogFileNumber = 0;

    public CommandHandler(){
        out = new PrintWriter(System.out);
        configTools = new ConfigTools("ServerAppLogConfig");
        configList = new ConfigList(configTools);
    }

    public void command(String cmd){
        CommandHandler.cmd = cmd;
    }

    //载入日志文件路径
    private void log_load(String path){
        logLoadNumber++;
        CommandHandler.logTools = LOGLoader.load(path); //载入路径
    }

    //显示日志文件和文件信息
    private void log_list(){
        try {
            String[] logList = CommandHandler.logTools.getLogList();
            for(String logFileInfo : logList){
                out.println(logFileInfo);
            }
            out.flush();
        }catch (LogListNullException e){
            System.err.println(e.getMessage());

        }
    }

    //打开日志文件
    private void log_open(String logFileName){
        try {
            CommandHandler.logTools.open(logFileName);
            openLogFileNumber++;
        }catch (LogFileNotFoundException e){
            System.err.println(e.getMessage());
        }
    }

    //显示日志文件内容
    private void log_show(){
        CommandHandler.logTools.show();
    }

    //显示配置文件的配置名称
    private void config_list(){
        Set<String> configNameList = CommandHandler.configTools.getConfigName();
        for(String name : configNameList){
            out.println(name);
        }
        out.flush();
    }

    private String config_load(String key) throws ConfigValueNotFoundException {
        return CommandHandler.configList.getConfig(key);
    }

    //处理命令
    public void handler(){
        String cmd = CommandHandler.cmd;
        String cmdType = cmd.substring(0 , cmd.indexOf(" "));
        cmd = cmd.substring(cmd.indexOf(" ")+1); //获取之后的命令
        //log类型的命令
        if(cmdType.equals(CommandType.LOG)){
            String logCmd = cmd.substring(0, cmd.indexOf(" "));
            //判断是否载入路径
            if(!logCmd.equals(Command.LOG_LOAD)&&logLoadNumber == 0){
                out.println(CommandExceptionConst.PATH_NOT_LOAD);
                out.flush();
                return;
            }
            //判断文件是否打开
            if(!logCmd.equals(Parameter._NAME)&&!logCmd.equals(Command.LIST)&&!logCmd.equals(Command.LOG_LOAD)&&
                    openLogFileNumber == 0){
                out.println(CommandExceptionConst.LOG_NOT_OPEN);
                out.flush();
                return;
            }

            cmd = cmd.substring(cmd.indexOf(" ")+1);
            switch (logCmd){
                case Command.LIST: this.log_list(); break;
                case Command.SHOW: this.log_show(); break;
                case Command.SEEK:{
                    String parameter = cmd.substring(0, cmd.indexOf(" ")); //获取参数
                    cmd = cmd.substring(cmd.indexOf(" ")+1);
                    String[] parameterValue;
                    //三个参数
                    if(parameter.equals(Parameter._3)){
                        parameterValue = new String[3]; //获取参数值
                        for(int i = 0; i < 3; i++){
                            //判断参数是否正确
                            if(i != 2&&cmd.indexOf(" ") >= cmd.trim().length()-1){
                                new CommandErrorPrompt(cmd, CommandExceptionConst.PARAMETER_ERROR);
                                return;
                            }
                            parameterValue[i] = cmd.substring(0, cmd.indexOf(" "));
                            cmd = cmd.substring(cmd.indexOf(" ")+1);
                        }
                        CommandHandler.logTools.seek(parameterValue[0], parameterValue[1], parameterValue[2]);
                        return;
                    }
                    //五个参数
                    if(parameter.equals(Parameter._5)) {
                        Object start;
                        Object[] end = new Object[2];
                        parameterValue = new String[5];
                        //获取参数值
                        for(int i = 0; i < 5; i++){
                            if(i != 4&&cmd.indexOf(" ") == cmd.length()-1){
                                new CommandErrorPrompt(cmd, CommandExceptionConst.PARAMETER_ERROR);
                                return;
                            }
                            parameterValue[i] = cmd.substring(0, cmd.indexOf(" "));
                            cmd = cmd.substring(cmd.indexOf(" ")+1);
                        }
                        //获取开始位置和结束位置
                        start = String.valueOf(parameterValue[2]);
                        for(int i = 0; i < 2; i++){
                            end[i] = String.valueOf(parameterValue[3].charAt(i));
                        }
                        boolean showAll = parameterValue[4].equals("true");
                        CommandHandler.logTools.seek(parameterValue[0], parameterValue[1], start, end, showAll);
                        return;
                    }
                    //显示六个参数
                    if(parameter.equals(Parameter._6)){
                        Object[] start = new Object[2];
                        Object[] end = new Object[3];
                        parameterValue = new String[6];
                        //获取参数值
                        for(int i = 0; i < 6; i++){
                            if(i != 5&&cmd.indexOf(" ") >= cmd.trim().length()-1){
                                new CommandErrorPrompt(cmd, CommandExceptionConst.PARAMETER_ERROR);
                                return;
                            }
                            parameterValue[i] = cmd.substring(0, cmd.indexOf(" "));
                            cmd = cmd.substring(cmd.indexOf(" ")+1);
                        }
                        //获取开始位置和结束位置
                        for(int i= 0; i < 2; i++){
                            start[i] = String.valueOf(parameterValue[3].charAt(i));

                        }
                        for(int i = 0; i < 3; i++){
                            end[i] = String.valueOf(parameterValue[4].charAt(i));
                        }
                        boolean showAll = parameterValue[5].equals("true");
                        CommandHandler.logTools.seek(parameterValue[0], parameterValue[1], parameterValue[2],
                                start, end, showAll);
                        return;
                    }
                    new CommandErrorPrompt(parameter);
                    break;
                }
                case Parameter._NAME: this.log_open(cmd.substring(0, cmd.indexOf(" "))); break;
                case Parameter._LOAD: {
                    String filePath = cmd.substring(0, cmd.indexOf(" "));
                    this.log_load(filePath);
                    break;
                }
                default: new CommandErrorPrompt(logCmd); break;
            }
            return;
        }
        if(cmdType.equals(CommandType.CONFIG)){
            String configCmd = cmd.substring(0, cmd.indexOf(" "));
            cmd = cmd.substring(cmd.indexOf(" ")+1);
            switch (configCmd){
                case Command.LIST: this.config_list(); break;
                case Command.PUT:{
                    int index = 0;
                    String value;
                    String[] parameterValue = new String[2];
                    //获取参数值
                    for(int i = 0; i < 4; i++){
                        if(i != 3&&cmd.indexOf(" ") >= cmd.trim().length()-1){
                            new CommandErrorPrompt(cmd, CommandExceptionConst.PARAMETER_ERROR);
                            return;
                        }
                        value = cmd.substring(0, cmd.indexOf(" "));
                        if(i == 0&&!value.equals(Parameter._NAME)||i == 2&&!value.equals(Parameter._VALUE)){
                            new CommandErrorPrompt(value);
                            return;
                        }
                        if(i == 1 || i == 3) {
                            parameterValue[index] = value;
                            index++;
                        }
                        cmd = cmd.substring(cmd.indexOf(" ")+1);
                    }
                    try {
                        CommandHandler.configTools.add(parameterValue[0], parameterValue[1]);
                    } catch (ConfigFileNotFoundException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                }
                case Command.CONFIG_LOAD: {
                    try {
                        String filePath = this.config_load(cmd.substring(0, cmd.indexOf(" ")));
                        logLoadNumber++;
                        CommandHandler.logTools = LOGLoader.load(filePath); //加载日志文件路径
                    } catch (ConfigValueNotFoundException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                }
                case Command.REMOVE: {
                    try {
                        Set<String> configNameList = CommandHandler.configTools.getConfigName();
                        String configName = cmd.substring(0 , cmd.indexOf(" "));
                        if(!configNameList.contains(configName)){
                            new CommandErrorPrompt(configName, ConfigExceptionConst.CONFIG_FILE_NOTFOUND);
                            return;
                        }
                        CommandHandler.configTools.remove(configName);
                    } catch (ConfigFileNotFoundException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                }
                default: new CommandErrorPrompt(configCmd); break;
            }
            return;
        }
        new CommandErrorPrompt(cmdType);
    }
}