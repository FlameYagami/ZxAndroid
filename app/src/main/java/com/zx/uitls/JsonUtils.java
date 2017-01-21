package com.zx.uitls;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 时空管理局 on 2016/7/12.
 */
public class JsonUtils
{
    private static final String TAG = JsonUtils.class.getSimpleName();

    /**
     * 序列化
     *
     * @param obj 对象
     * @return Json
     */
    public static String serializer(Object obj) {
        Gson gson = new GsonBuilder().create();
        Log.e(TAG, gson.toJson(obj));
        return gson.toJson(obj);
    }

    /**
     * 反序列化对象
     *
     * @param jsonData Json
     * @return 对象
     */
    public static <T> T deserializer(String jsonData, Class<T> classType) {
        try {
            Gson gson   = new Gson();
            T    result = gson.fromJson(jsonData, classType);
            return result;
        } catch (Exception e) {
            LogUtils.e(TAG, "deserializer->error");
            return null;
        }
    }

    /**
     * 反序列化数组对象
     *
     * @param jsonData Json
     * @return 数组集合
     */
    public static <T> List<T> deserializerArray(String jsonData, Class<T[]> clazz) {
        try {
            Gson gson  = new Gson();
            T[]  array = gson.fromJson(jsonData, clazz);
            return Arrays.asList(array);
        } catch (Exception e) {
            LogUtils.e(TAG, "deserializerArray->error");
            return null;
        }
    }

    /**
     * 反序列化集合对象
     *
     * @param jsonData Json
     * @return 对象集合
     */
    public static <T> List<T> deserializerList(String jsonData, Class<T> classType) {
        List<T> arrayList = new ArrayList<>();
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>()
            {
            }.getType();
            List<JsonObject> jsonObjects = new Gson().fromJson(jsonData, type);
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(new Gson().fromJson(jsonObject, classType));
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "deserializerList->error");
            return null;
        }
        return arrayList;
    }
}
