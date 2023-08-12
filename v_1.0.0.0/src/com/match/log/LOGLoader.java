package com.match.log;

import com.match.tools.LogTools;

public class LOGLoader {
    public static LogTools load(String logPath){
        return new LogTools(logPath);
    }
}
