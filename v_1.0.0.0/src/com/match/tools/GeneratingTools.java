package com.match.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 这个类是生成json数据的
 * @author match
 */
public class GeneratingTools {

    private String data;
    private JSONObject jsonData;

    public GeneratingTools() {
        jsonData = new JSONObject();
    }

    public GeneratingTools(String data){
        this.data = data;
        jsonData = new JSONObject();
    }

    public JSONObject json(){
        return JSON.parseObject(data);
    }

    public void json(String key, Object value){
        jsonData.put(key, value);
    }

    public JSONObject getJson() { return jsonData; }
    public void initJson() {
        jsonData.clear();
    }
}
