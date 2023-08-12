package com.match.test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException {
        long timeS = System.currentTimeMillis();//单位为ms
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./test.txt"), 512);
        int num = 40000;
        int index = 0;
        while (index < num){
            bufferedWriter.write(String.valueOf(index));
            bufferedWriter.newLine();
            bufferedWriter.flush();
            index ++;
        }
        bufferedWriter.flush();
        bufferedWriter.close();
        long timeE = System.currentTimeMillis();
        System.out.println(timeS);
        System.out.println(timeE);
        System.out.println(new String("耗时：".getBytes(StandardCharsets.UTF_8)) + (timeE - timeS) + " ms");


    }
}
