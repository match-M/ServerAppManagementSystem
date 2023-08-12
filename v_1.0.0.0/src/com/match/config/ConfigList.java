package com.match.config;

import com.match.constants.ConfigExceptionConst;
import com.match.exception.*;
import com.match.tools.ConfigTools;
import java.io.IOException;
import java.util.HashMap;

public class ConfigList {

    private ConfigTools configTools;
    private static HashMap<String, String> configList;

    public ConfigList(ConfigTools configTools) {
        this.configTools = configTools;
        get();
    }

    public ConfigList(String configFileName , ConfigTools configTools){
        this.configTools = configTools;
        get();
    }

    /**
     * 获取配置信息
     */
    private void get() {
        try {
            configList = this.configTools.getConfigReader();
        }catch (IOException e){
            e.printStackTrace();
        } catch (ConfigFileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * 更新配置信息
     * @param configFileName 配置文件名
     */
    private void up(String configFileName){
        //判断配置列表是否为空
        if(configList != null)
            configList.clear();
        try {
            configList = this.configTools.getConfigReader(configFileName);
        }catch (IOException e){
            e.printStackTrace();
        } catch (ConfigFileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void upConfigList(String configFileName) {
        this.configTools = new ConfigTools(configFileName);
        up(configFileName);
    }

    /**
     * 获取配置值
     * @param key 配置名称
     * @return 返回配置值
     * @throws ConfigValueNotFoundException 如果配置文件损损坏时会抛出这个异常
     */
    public String getConfig(String key) throws ConfigValueNotFoundException {
        String value;
        if(configList.get(key) == null)
            throw new ConfigValueNotFoundException(ConfigExceptionConst.CONFIG_VALUE_NOTFOUND);
        value = configList.get(key);

        return value;
    }
}
