package com.ggtf.xieyingwu.facebooktest.util;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by xieyingwu on 2017/6/21.
 */

public class GsonUtil {

    public static <T> T jsonToObject(JSONObject jsonObject, Class<T> t) {
        Gson gson = new Gson();
        return gson.fromJson(jsonObject.toString(), t);
    }
}
