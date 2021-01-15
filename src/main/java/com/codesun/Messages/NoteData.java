package com.codesun.Messages;
//用来获取最终返回的短信信息

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteData {
    private String case_id;
    private String imei;
    private String device_state;
    private String phone;
    private String content;
    private String content_time;
    private String memo;
}
