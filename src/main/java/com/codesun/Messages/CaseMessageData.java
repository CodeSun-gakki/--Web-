package com.codesun.Messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseMessageData {
    private String case_id;
    private String sys_type="1,2";
    private String stime;
    private String imsi;
    private String phone;
    private String phone_nation_code;
    private String memo;
}
