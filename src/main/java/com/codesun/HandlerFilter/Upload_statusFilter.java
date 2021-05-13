package com.codesun.HandlerFilter;

import com.codesun.Controller.ServerController;
import com.codesun.Enum.CmdType;
import com.codesun.Filter.RequestFilter;
import com.codesun.Messages.ErrorMessage;
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
public class Upload_statusFilter implements RequestFilter {
    //这里上传设备信息
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private BASE64Encoder base64Encoder;
    @Override
    public String doFilter(Task task) {
        if(task.getCmdType().equals(CmdType.Upload_status.getCmdType())){
            log.info("接收到上传信息请求");
            String device_state=null;
            String content=null;
            try {
                JsonNode node = mapper.readTree(task.getJsonText());
                JsonNode data = node.get("data");
                device_state = data.get("device_state").asText();
                content=data.get("content").asText();
            } catch (JsonProcessingException e) {
                log.error("处理上传设备信息出错");
                task.setState("1002");
                return "处理上传设备信息出错";
            }
            ErrorMessage errorMessage = new ErrorMessage();
            DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            errorMessage.setStime(format.format(new Date()));
            errorMessage.setDevice_id(task.getDevice_id());
            errorMessage.setData("10");
            //如果设备报异常，日志打印日志信息
            if(device_state.equals("0")||!content.equals("")){
                errorMessage.setCmd_type("300");
                task.setState("1002");
                log.info("返回设备状态不稳定消息");
                ServerController.taskStatus=102;
            }else{
                //如果设备正常运转的话，直接返回设备需要的信息
                errorMessage.setCmd_type("204");
                task.setState("1004");
                ServerController.taskStatus=101;
                log.info("返回信息就绪消息");
            }
            String ans=null;
            try {
                ans=mapper.writeValueAsString(errorMessage);
            } catch (JsonProcessingException e) {
                log.error("处理上传设备信息-返回错误信息出错");
                task.setState("1002");
                return "处理上传设备信息-返回错误信息出错";
            }
            //return  ans;
            return Base64.getEncoder().encodeToString(ans.getBytes());
        }
        return null;
    }
}
