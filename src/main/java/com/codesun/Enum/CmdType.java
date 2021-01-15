package com.codesun.Enum;

import javafx.concurrent.Task;
import lombok.Data;


public enum CmdType {
    //设备注册
    Device_registration("101"),
    //上传状态
    Upload_status("103"),
    //上传短信
    Upload_SMS("105"),
    //任务信息
    Task_information("202"),
    //任务就绪
    Task_ready("204"),
    //普通响应
    General_response("300")
   ;

    private String CmdType;
    private CmdType(String cmdType){
        this.CmdType=cmdType;
    }

    public void setCmdType(String cmdType) {
        CmdType = cmdType;
    }

    public String getCmdType() {
        return CmdType;
    }
}
