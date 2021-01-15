package com.codesun.HandlerFilter;

import com.codesun.Enum.CmdType;
import com.codesun.Filter.RequestFilter;
import com.codesun.Messages.CaseMessage;
import com.codesun.Messages.CaseMessageData;
import com.codesun.Messages.ErrorMessage;
import com.codesun.Tasks.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class Device_registrationFilter implements RequestFilter {
    //这里完成设备注册的处理
    private static int case_id=1;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private BASE64Encoder base64Encoder;
    @Override
    public String doFilter(Task task) {
        if(task.getCmdType().equals(CmdType.Device_registration.getCmdType())){
            log.info("接受到注册请求");
            if(task.getTask()==null){
                //返回没有请求的任务Json
                DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                ErrorMessage errorMessage = new ErrorMessage(format.format(new Date()), task.getDevice_id(),
                        CmdType.General_response.getCmdType(), "20");
                String ans=null;
                try {
                   ans=mapper.writeValueAsString(errorMessage);
                } catch (JsonProcessingException e) {
                   log.error("处理设备注册请求出错");
                   task.setState("1002");
                   return "处理设备注册请求出错";
                }
                task.setState("1002");
                //return ans;
                log.info("没有任务，返回无任务响应");
                return Base64.getEncoder().encodeToString(ans.getBytes());
            }else{
                //返回下发案件的Json
                CaseMessage caseMessage = new CaseMessage();
                DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                caseMessage.setStime(format.format(new Date()));
                caseMessage.setDevice_id(task.getDevice_id());
                caseMessage.setCmd_type(CmdType.Task_information.getCmdType());
                CaseMessageData data=new CaseMessageData();
                data.setCase_id((case_id++)+"");
                data.setStime(format.format(new Date()));
                data.setImsi(task.getTask().getImsi());
                data.setPhone(task.getTask().getPhone());
                data.setPhone_nation_code(task.getTask().getPhone_nation_code());
                List<CaseMessageData> list=new ArrayList<>();
                list.add(data);
//                caseMessage.setData(list);
                String d=null;
                try {
                    d=mapper.writeValueAsString(list);
                } catch (JsonProcessingException e) {
                    log.error("处理设备注册请求出错");
                    task.setState("1002");
                    return "处理设备注册请求出错";
                }
                caseMessage.setData(d);
                String ans=null;
                try {
                    ans=mapper.writeValueAsString(caseMessage);
                } catch (JsonProcessingException e) {
                    log.error("处理设备注册请求出错");
                    task.setState("1002");
                    return "处理设备注册请求出错";
                }
                //return ans;
                log.info("有任务，下发案件成功");
                return Base64.getEncoder().encodeToString(ans.getBytes());
            }
        }
        return null;
    }
}
