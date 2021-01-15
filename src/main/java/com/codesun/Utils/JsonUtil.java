package com.codesun.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    public static ObjectMapper mapper=new ObjectMapper();
    public static String getCmdType(String JsonText) throws JsonProcessingException {
        //这里异常抛出去
        JsonNode node = mapper.readTree(JsonText);
        return node.get("cmd_type").asText();
    }
    public static String getDeviceID(String JsonText) throws JsonProcessingException {
        JsonNode node = mapper.readTree(JsonText);
        return node.get("device_id").asText();
    }

    public static void main(String[] args) throws JsonProcessingException {
        String JsonText="{\"stime\":\"2018-10-28 16:04:35\",\"device_id\":\"QZ000002\",\"cmd_type\":\"101\",\"data\":\" \",\"city_code\":\"460000\",\"city_name\":\"武汉市\"}";
        //String JsonText="{\"cmd_type\":\"105\",\"data\":\"{\\\"case_id\\\":\\\"5\\\",\\\"cmd_type\\\":\\\"105\\\",\\\"content\\\":\\\"【腾讯】您请求登录的微信验证码为：213131\\\",\\\"content_time\\\":\\\"2018-10-28 16:04:35\\\",\\\"device_id\\\":\\\"QZ000002\\\",\\\"device_state\\\":\\\"1\\\",\\\"memo\\\":\\\"\\\"}\",\"device_id\":\"QZ000002\",\"stime\":\"2018-10-28 16:04:35\"}";
        System.out.println(getCmdType(JsonText));
    }
}
