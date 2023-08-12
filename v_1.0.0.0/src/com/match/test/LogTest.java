package com.match.test;

import com.match.exception.LogFileNotFoundException;
import com.match.exception.LogListNullException;
import com.match.log.LOGLoader;
import com.match.tools.LogTools;

public class LogTest {
    public static void main(String[] args) throws LogListNullException, LogFileNotFoundException {
        System.out.println("-------------------LOGLoader.load()-------------------");
        LogTools logToolsTest = LOGLoader.load("E:\\Project\\ProjectForJava\\OnlineChatServer\\log\\");
        System.out.println("-------------------logToolsTest.open()-------------------");
        logToolsTest.open("RuntimeLog");
        System.out.println("-------------------logToolsTest.show()-------------------");
        logToolsTest.show();
        Object[] start;
        Object[] end;
        System.out.println("-------------------logToolsTest.seek(6参数)-------------------");
        start = new Object[]{"", ":", "["};
        end = new Object[]{":", "_", "]"};
        logToolsTest.seek("192.168.88.179", "58274", "USER_OPERATION", start, end, true);
        System.out.println("-------------------logToolsTest.seek(5参数)-------------------");
        start = new Object[]{"", "["};
        end = new Object[]{":", "]"};
        logToolsTest.seek("192.168.88.179", "USER_OPERATION", start, end, true);
        System.out.println("-------------------logToolsTest.seek(3参数)-------------------");
        logToolsTest.seek("2023-08-07 at 18:31:10", "_", " CST");
        System.out.println("-------------------logToolsTest.getLogList()-------------------");
        String[] logList = logToolsTest.getLogList();
        for (String s : logList) {
            System.out.println(s);
        }
        logToolsTest.close();

    }
}
