package com.codesun.Controller;


import com.codesun.FilterChain.RequestFilterChain;
import com.codesun.Tasks.InterceptTask;
import com.codesun.Tasks.Task;
import com.codesun.Utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.io.IOException;

@ToString
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
    private volatile int CODEState=-1;
    @PostMapping("/send!m.action")
    public String acceptRequest(String s){
        String JsonText;
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
                CODE=new String(task.getContent().toCharArray());
                CODEState=1;
            }
            return result;
        }
        log.error("未能找到对应的处理器");
        return null;
    }

    @RequestMapping("/getVerificationCode")
    public String AppiumRequest(InterceptTask interceptTask){
      if(interceptTask!=null&&interceptTask.hasMessage()){
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
        if(this.interceptTask==null)
            //没有注册任务就直接返回出错
            return "1002";
        return this.task.getState();
    }

    @RequestMapping("/getCodeState")
    public String getCodeState(){
        if(CODEState!=-1){
            log.info("获取短信状态: ok");
            return "ok";
        }else{
            log.info("获取短信状态: error");
            return "error";
        }
    }
    @RequestMapping("/getCodeContent")
    public String getCodeContent(){
      if(CODEState!=-1&&CODE!=null){
          CODEState=-1;
          String ans=new String(CODE.toCharArray());
          CODE=null;
          log.info("获取短信内容成功,内容为:"+ans);
          return base64Encoder.encode(ans.getBytes());
      }
      log.info("获取短信内容为:NotGet");
      return  base64Encoder.encode("NotGet".getBytes());
    }
    @RequestMapping("/cancelTask")
    public String cancelTask(){
        this.interceptTask=null;
        return "ok";
    }
}
