package com.match.test;

import com.match.command.CommandHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CommandTest {
    public static void main(String[] args) {
        CommandHandler commandHandler = new CommandHandler();
        Scanner inCmd = new Scanner(System.in);
        String cmd;
        System.out.print("[admin]=>");
        while (!(cmd = inCmd.nextLine()).equals("exit")) {
            long timeS = System.currentTimeMillis();//单位为ms
            commandHandler.command(cmd+" ");
            commandHandler.handler();
            long timeE = System.currentTimeMillis();
            System.out.println(new String("本次命令耗时：".getBytes(StandardCharsets.UTF_8)) + (timeE - timeS) + " ms");
            System.out.print("[admin]=>");
        }

    }
}
