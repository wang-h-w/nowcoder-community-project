package com.nowcoder.community.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

    /**
     * 生成随机字符串
     * @return Random string
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * MD5加密
     * @param key String before MD5
     * @return String after MD5
     */
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) return null;
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJsonString(String key, Object value) {
        JSONObject json = new JSONObject();
        json.put(key, value);
        return json.toString();
    }
}
