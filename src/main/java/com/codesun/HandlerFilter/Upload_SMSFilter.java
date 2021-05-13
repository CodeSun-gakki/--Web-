package com.codesun.HandlerFilter;

import com.codesun.Controller.ServerController;
import com.codesun.Enum.CmdType;
import com.codesun.Filter.RequestFilter;
import com.codesun.Messages.ErrorMessage;
import com.codesun.Messages.NoteData;
import com.codesun.Tasks.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class Upload_SMSFilter implements RequestFilter {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private BASE64Encoder base64Encoder;
    @Override
    public String doFilter(Task task) {
        if(task.getCmdType().equals(CmdType.Upload_SMS.getCmdType())){
            log.info("接受到短信上传请求");
            NoteData nodeData=null;
            try {
                JsonNode node = mapper.readTree(task.getJsonText());
                JsonNode data = node.get("data");
                data.toString();
                nodeData = mapper.readValue(data.toString(), NoteData.class);
            } catch (JsonProcessingException e) {
                log.error("解析短信内容出错");
                task.setState("1002");
                return "解析短信内容出错";
            }
           task.setContent(nodeData.getContent());
            ErrorMessage message = new ErrorMessage();
            DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            message.setStime(format.format(new Date()));
            message.setDevice_id(task.getDevice_id());
            message.setCmd_type("300");
            message.setData("10");
            String ans=null;
            try {
                ans=mapper.writeValueAsString(message);
            } catch (JsonProcessingException e) {
                log.error("解析完短信内容，生成json信息出错！");
                task.setState("1002");
                return "解析完短信内容，生成json信息出错！";
            }
            task.setState("1003");
            //return ans;

            log.info("返回信息获取成功消息");
            return Base64.getEncoder().encodeToString(ans.getBytes());
        }
        return null;
    }
}
