package com.zx.bean;

import android.text.TextUtils;

import com.zx.config.MapConst;
import com.zx.config.SpConst;
import com.zx.uitls.JsonUtils;
import com.zx.uitls.SpUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static br.com.zbra.androidlinq.Linq.stream;

/**
 * Created by 八神火焰 on 2017/1/12.
 */

public class KeySearchBean
{
    private static LinkedHashMap<String, Boolean> keySearchMap = new LinkedHashMap<>();

    static {
        for (LinkedHashMap.Entry<String, String> entry : MapConst.KeySearchMap.entrySet()) {
            keySearchMap.put(entry.getKey(), true);
        }
    }

    private static void initKeySearchMap() {
        String keySearchJson = SpUtil.getInstances().getString(SpConst.KeySearch);
        // Json异常处理
        if (TextUtils.isEmpty(keySearchJson)) {
            keySearchJson = JsonUtils.serializer(new ArrayList<String>());
        }
        // 解析保存的关键字查询范围
        List<String> keySearchList = JsonUtils.deserializerArray(keySearchJson, String[].class);
        assert keySearchList != null;
        for (LinkedHashMap.Entry<String, Boolean> entry : keySearchMap.entrySet()) {
            for (String keySearch : keySearchList) {
                if (entry.getKey().equals(keySearch)) {
                    keySearchMap.put(entry.getKey(), true);
                    break;
                }
            }
        }
    }

    public static List<String> getSelectKeySearchList(){
        initKeySearchMap();
        return stream(keySearchMap).where(Map.Entry::getValue).select(Map.Entry::getKey).toList();
    }

    public static LinkedHashMap<String, Boolean> getKeySearchMap() {
        initKeySearchMap();
        return keySearchMap;
    }

    public static void saveKeySearchMap(LinkedHashMap<String, Boolean> keySearchMap) {
        String keySearchJson = JsonUtils.serializer(stream(keySearchMap).where(Map.Entry::getValue).select(Map.Entry::getKey).toList());
        SpUtil.getInstances().putString(SpConst.KeySearch, keySearchJson);
    }
}
