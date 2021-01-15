package com.codesun;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
    public static void main(String[] args) throws JsonProcessingException {
      String jsonText="{\"cmd_type\":\"105\",\"data\":\"{\\\"case_id\\\":\\\"5\\\",\\\"cmd_type\\\":\\\"105\\\",\\\"content\\\":\\\"【腾讯】您请求登录的微信验证码为：213131\\\",\\\"content_time\\\":\\\"2018-10-28 16:04:35\\\",\\\"device_id\\\":\\\"QZ000002\\\",\\\"device_state\\\":\\\"1\\\",\\\"memo\\\":\\\"\\\"}\",\"device_id\":\"QZ000002\",\"stime\":\"2018-10-28 16:04:35\"}";
        ObjectMapper mapper=new ObjectMapper();
        JsonNode node = mapper.readTree(jsonText);
        System.out.println(node.get("cmd_type").toString());
    }
}
