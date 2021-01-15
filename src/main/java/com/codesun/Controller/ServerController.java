package com.codesun.Controller;


import com.codesun.FilterChain.RequestFilterChain;
import com.codesun.Tasks.InterceptTask;
import com.codesun.Tasks.Task;
import com.codesun.Utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.org.apache.bcel.internal.classfile.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
public class ServerController {

    @Autowired
     private RequestFilterChain filterChain;
    @Autowired
     private BASE64Encoder base64Encoder;
    @Autowired
     private BASE64Decoder base64Decoder;

    private InterceptTask interceptTask;

    private Task task;
    private volatile String CODE;
    private volatile String CODEState;
    @PostMapping("/send!m.action")
    public String acceptRequest(String s){
        String JsonText=null;
        if(s==null){
            log.error("请求参数接受错误");
            return null;
        }
      // 进行解码
        try {
            byte[] bytes = base64Decoder.decodeBuffer(s);
            JsonText=new String(bytes);
            log.info("收到请求：{}",JsonText);
        } catch (IOException e) {
            log.error("base64解码错误");
            return null;
        }
        //组装一个Task
        Task task=new Task();
        task.setJsonText(JsonText);
        try {
            String cmdType = JsonUtil.getCmdType(JsonText);
            task.setCmdType(cmdType);
            String deviceID = JsonUtil.getDeviceID(JsonText);
            task.setDevice_id(deviceID);
        } catch (JsonProcessingException e) {
            log.error("获取命令类型错误");
            return null;
        }
        if(this.interceptTask!=null){
            task.setTask(this.interceptTask);
        }
        this.task=task;
        //组装完成
        String result = filterChain.doFilter(task);
        filterChain.flip();
        if(result!=null){
            //这里进行加密进行返回，这里后面需要改回来
            //return base64Encoder.encode(result.getBytes());
            if(!task.getContent().equals("NotGet")){
                CODE=task.getContent();
                CODEState="GET";
            }
            return result;
        }
        log.error("未能找到对应的处理器");
        return null;
    }

    @PostMapping("/getVerificationCode")
    public String AppiumRequest(InterceptTask interceptTask){
      if(interceptTask!=null){
          this.interceptTask=interceptTask;
          return "ok";
      } else{
          return "error";
      }
    }
    @RequestMapping("/getState")
    public String getState(){
        //正在运行   1001     //出错     1002  // 短信抓取设备已经准备就绪  1004      //拿到结果     1003
        if(this.task==null)
            return "1001";
        return this.task.getState();
    }

    @RequestMapping("/getCodeState")
    public String getCodeState(){
        if(CODEState!=null&&"GET".equals(CODEState)){
            return "ok";
        }else{
            return "error";
        }
    }
    @RequestMapping("/getCodeContent")
    public String getCodeContent(){
        if(this.task==null)
            return base64Encoder.encode("NotGet".getBytes());
        else{
            CODEState=null;
            String temp= CODE;
            CODE=null;
            if(temp==null)
            {
                return base64Encoder.encode("NotGet".getBytes());
            }
            return base64Encoder.encode(temp.getBytes());
        }

    }
}
