package com.match.tools;

import com.match.constants.ConfigConst;
import com.match.constants.ConfigExceptionConst;
import com.match.exception.ConfigFileNotFoundException;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class ConfigTools {

    private String configFileName; //配置文件的名字，不需要后缀只要写文件名即可
    public ConfigTools(){}
    public ConfigTools(String configFileName){
        this.configFileName = configFileName;
    }

    /**
     * 新建配置文件
     * @param configFileName 新建配置文件的名字
     * @return true - 新建成功，false - 创建失败
     */
    private boolean newConfigFile(String configFileName){
        if(configFileName == null)
            configFileName = this.configFileName;
        File configFile = new File(ConfigConst.PATH+configFileName+ConfigConst.EXTENSION);
        boolean result = false;
        try {
            result = configFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 创建新的配置文件
     * @return true-创建成功，false-创建失败
     */
    public boolean createConfigFile(){
        return newConfigFile(null);
    }

    /**
     * 创建新的配置文件，这个方法一般用于修复配置文件时使用
     */
    public void createConfigFile(String configFileName){
        newConfigFile(configFileName);
    }

    /**
     * 因为不确定配置名称和配置内容的数量所以采用数组的形式
     * @param configKey 配置名称
     * @param configValue 配置内容
     * @param explanatoryNote 注释
     * @exception ConfigFileNotFoundException 如果配置文件无法找到则抛出该异常
     */
    public void writeConfig(String[] configKey, Object[] configValue,
                            String explanatoryNote) throws ConfigFileNotFoundException {
        Properties config = new Properties();
        int i = 0;
        try {
            FileWriter fileWriter = new FileWriter(ConfigConst.PATH+configFileName+ConfigConst.EXTENSION);
            for(String key : configKey){
                config.setProperty(key, String.valueOf(configValue[i]));
                i++;
            }
            config.store(fileWriter, explanatoryNote);
            fileWriter.close();
        } catch (IOException e) {
            if(e instanceof FileNotFoundException)
                throw new ConfigFileNotFoundException(ConfigExceptionConst.CONFIG_FILE_NOTFOUND);
            e.printStackTrace();
        }
    }

    /**
     * 修改配置，支持批量修改
     * @param keys 配置名称
     * @param values 新的配置内容
     * @exception ConfigFileNotFoundException 如果配置文件无法找到则抛出该异常
     */
    public void upConfig(String[] keys, Object[] values) throws ConfigFileNotFoundException {
        int i = 0;
        FileWriter fileWriter;
        Properties config = new Properties();
        try {
            HashMap<String, String> configList = getConfigReader();
            fileWriter = new FileWriter(ConfigConst.PATH+configFileName+ConfigConst.EXTENSION);
            for (String key : keys){
                configList.replace(key, String.valueOf(values[i]));
                i++;
            }
            Set<String> Keys = configList.keySet();
            Iterator<String> it = Keys.iterator();
            String Key;
            while(it.hasNext()){
                Key = it.next();
                config.setProperty(Key, configList.get(Key));
            }
            config.store(fileWriter, null);
            fileWriter.close();
        } catch (IOException e) {
            if(e instanceof FileNotFoundException)
                throw new ConfigFileNotFoundException(ConfigExceptionConst.CONFIG_FILE_NOTFOUND);
            e.printStackTrace();
        }
    }

    /**
     * 添加配置
     * @param key 配置名称
     * @param value 新的配置内容
     * @exception ConfigFileNotFoundException 如果配置文件无法找到则抛出该异常
     */
    public void add(String key, Object value) throws ConfigFileNotFoundException {
        FileWriter fileWriter = null;
        Properties config = new Properties();
        try {
            fileWriter = new FileWriter(ConfigConst.PATH+configFileName+ConfigConst.EXTENSION, true);
            config.setProperty(key, String.valueOf(value));
            config.store(fileWriter, null);
        } catch (IOException e){
            if(e instanceof FileNotFoundException)
                throw new ConfigFileNotFoundException(ConfigExceptionConst.CONFIG_FILE_NOTFOUND);
            e.printStackTrace();
        }finally {
            try {
                if (fileWriter != null) fileWriter.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除配置
     * @param key 要删除的配置
     */
    public void remove(String key) throws ConfigFileNotFoundException {
        int i = 0;
        String[] keys;
        String[] values;
        try {
            HashMap<String, String> configList = getConfigReader();
            configList.remove(key);
            keys = new String[configList.size()];
            values = new String[configList.size()];
            for(String k : configList.keySet()){
                keys[i] = k;
                values[i] = configList.get(k);
            }
            writeConfig(keys, values, null);
        } catch (IOException e) {
            if(e instanceof FileNotFoundException)
                throw new ConfigFileNotFoundException(ConfigExceptionConst.CONFIG_FILE_NOTFOUND);
            e.printStackTrace();
        }


    }

    /**
     * 读取配置文件
     * @return 返回包含配置名称和配置内容的hashmap，key-配置名称，value-配置内容
     * @exception ConfigFileNotFoundException 如果配置文件无法找到则抛出该异常
     */
    private HashMap<String, String> getConfig(String configFileName) throws IOException,
            ConfigFileNotFoundException {
        FileReader fileReader = null;
        Properties prop = new Properties();
        HashMap<String, String> config = new HashMap<>();
        if (configFileName == null)
            configFileName = this.configFileName;
        try {
            fileReader = new FileReader(ConfigConst.PATH+configFileName+ConfigConst.EXTENSION);
            prop.load(fileReader);
            Set<String> keySet = prop.stringPropertyNames();
            for (String key : keySet) {
                config.put(key, prop.getProperty(key));
            }
        }catch (IOException e){
            if(e instanceof FileNotFoundException)
                throw new ConfigFileNotFoundException(ConfigExceptionConst.CONFIG_FILE_NOTFOUND);
            e.printStackTrace();
        }finally {
            if (fileReader != null) fileReader.close();
        }
        return config;
    }

    /**
     * 获取配置名称
     * @param configFileName 配置文件名称
     * @return 返回带有配置名称的set集合
     */
    private Set<String> configNameList(String configFileName){
        Set<String> keySet = null;
        FileReader fileReader = null;
        Properties prop = new Properties();

        if(configFileName == null)
            configFileName = this.configFileName;
        try {
            fileReader = new FileReader(ConfigConst.PATH+configFileName+ConfigConst.EXTENSION);
            prop.load(fileReader);
            keySet = prop.stringPropertyNames();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (fileReader != null) fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return keySet;
    }

    /**
     * 获取配置文件，文件名是this.configFileName
     * @return 返回包含配置名称和配置内容的hashmap，key-配置名称，value-配置内容
     */
    public HashMap<String, String> getConfigReader() throws IOException,
            ConfigFileNotFoundException {
        return this.getConfig(null);
    }

    /**
     * 获取配置文件内的配置信息
     * @return 返回包含配置名称和配置内容的hashmap，key-配置名称，value-配置内容
     */
    public HashMap<String, String> getConfigReader(String configFileName) throws IOException,
            ConfigFileNotFoundException {
        return this.getConfig(configFileName);
    }

    /**
     * 获取配置名称，配置文件名称是this.configFileName
     * @return 返回带有配置名称的set集合
     */
    public Set<String> getConfigName(){
        return this.configNameList(null);
    }

    /**
     * 获取配置名称
     * @return 返回带有配置名称的set集合
     */
    public Set<String> getConfigName(String configFileName){
        return this.configNameList(configFileName);
    }
}
