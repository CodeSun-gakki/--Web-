package com.codesun.Messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseMessage {
    private String stime;
    private String device_id;
    private String cmd_type;
    private String data;
}
