package com.match.tools;

import com.match.constants.FileInfoConst;
import com.match.constants.LogConst;
import com.match.constants.LogExceptionConst;
import com.match.exception.LogFileNotFoundException;
import com.match.exception.LogListNullException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LogTools {

    private String logPath;
    private BufferedReader logFile;
    private PrintWriter out;
    public LogTools(){
        out = new PrintWriter(System.out);
    }
    public LogTools(String logPath){
        out = new PrintWriter(System.out);
        this.logPath = logPath;
    }

    /**
     * 获取路径下的日志文件
     * @return 存储日志文件名的String数组
     * @throws LogListNullException 如果路径下的文件夹为空就会抛出这个异常
     */
    public String[] getLogList() throws LogListNullException {
        int index = 0;
        File logFiles = new File(logPath);
        File[] fileList = logFiles.listFiles(); //获取文件
        if (fileList == null) {
            throw new LogListNullException(LogExceptionConst.LOG_FILE_LIST_NULL);
        }

        String[] logList = new String[fileList.length];
        try {
            for (File file : fileList) {
                long lines = Files.lines(Paths.get(new File(logPath + file.getName()).getPath())).count();
                String fileInfo = file.getName()+" "+FileInfoConst.BYTES+":"+
                        file.length()+" "+FileInfoConst.LINES+":"+lines;
                logList[index] = fileInfo; //获取文件名和文件的大小单位为byte
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logList;
    }

    /**
     * 打开日志文件
     * @param logFileName 要打开的文件名字，只需要名字即可不需要后缀名
     * @return true-打开失败，false-打开成功
     * @throws LogFileNotFoundException 如果文件无法找到就会抛出这个异常
     */
    public boolean open(String logFileName) throws LogFileNotFoundException{
        File file = new File(logPath+logFileName+LogConst.EXTENSION); //路径+文件名+后缀名
        boolean openError = false;
        try {
            this.logFile = new BufferedReader(new FileReader(file), 512); //512为缓冲区大小
            this.logFile.mark((int)file.length()+1); //文件开头标记
        } catch (FileNotFoundException e) {
            openError = true;
            throw new LogFileNotFoundException(LogExceptionConst.LOG_FILE_NOTFOUND);
        } catch (IOException e) {
            openError = true;
            e.printStackTrace();
        }
        return openError;
    }

    /**
     * 显示日志文件内容
     */
    public void show() {
        String logInfos;
        try {
            while ((logInfos = this.logFile.readLine()) != null) {
                out.println(logInfos);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                this.logFile.reset(); //让指针回到标记
            }catch (IOException e){
                e.printStackTrace();
            }
            out.flush();
        }

    }

    /**
     * 查找日志中的某一或某几条信息
     * 查询条件0要是ip
     * @param condition_0 查询条件1
     * @param condition_1 查询条件2
     * @param condition_2 查询条件3
     * @param start 条件开始位置
     * @param end 条件结束位置
     * @param showAll true-显示符合条件的所有信息，false-显示符合条件的第一条信息
     * (0, end[0]) 是condition_0的前后位置
     * (start[0], end[1]) 是condition_1的前后位置
     * (start[1], end[2]) 是condition_2的前后位置
     */
    public void seek(String condition_0, String condition_1, String condition_2, Object[] start, Object[] end,
                     boolean showAll){
        String line;
        try {
            while ((line = this.logFile.readLine()) != null) {
                if(!line.substring(0, line.indexOf((String)end[0])).equals(condition_0)) continue;
                //因为substring函数包含start值所以要往后移一位
                if(!line.substring(line.indexOf((String)start[0])+1,
                        line.indexOf((String)end[1])).equals(condition_1)) continue;
                if(!line.substring(line.indexOf((String)start[1])+1,
                        line.indexOf((String)end[2])).equals(condition_2)) continue;
                out.println(line);
                if(!showAll)
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                this.logFile.reset();//让指针回到标记
            }catch (IOException e){
                e.printStackTrace();
            }
            out.flush();
        }
    }

    /**
     * 查找日志中的某一或某几条信息
     * 查询条件0要是ip
     * @param condition_0 查询条件1
     * @param condition_1 查询条件2
     * @param start 条件开始位置
     * @param end 条件结束位置
     * @param showAll true-显示符合条件的所有信息，false-显示符合条件的第一条信息
     * (0, end[0]) 是condition_0的前后位置
     * (start, end[1]) 是condition_1的前后位置
     */
    public void seek(String condition_0, String condition_1, Object start, Object[] end, boolean showAll){
        String line;
        try {
            while ((line = this.logFile.readLine()) != null) {
                if(!line.substring(0, line.indexOf((String)end[0])).equals(condition_0)) continue;
                if(!line.substring(line.indexOf((String)start)+1,
                        line.indexOf((String)end[1])).equals(condition_1)) continue;
                out.println(line);
                if(!showAll)
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                this.logFile.reset();//让指针回到标记
            }catch (IOException e){
                e.printStackTrace();
            }
            out.flush();
        }

    }

    /**
     * 模糊查询仅一个查询条件
     * @param condition 查询条件
     * @param start 条件开始位置
     * @param end 条件结束位置
     */
    public void seek(String condition, Object start, Object end){
        String line;
        try {
            while ((line = this.logFile.readLine()) != null) {
                if(!line.substring(line.indexOf((String)start)+1, line.indexOf((String)end)).equals(condition)) continue;
                out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                this.logFile.reset();//让指针回到标记
            }catch (IOException e){
                e.printStackTrace();
            }
            out.flush();
        }
    }

    public void close(){
        try {
            this.out.close();
            this.logFile.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}