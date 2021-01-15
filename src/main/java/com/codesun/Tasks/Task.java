package com.codesun.Tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private String JsonText;
    private String CmdType;
    private InterceptTask task;
    private String device_id;
    private String state="1001";
    //短信内容
    private String content="NotGet";
    public boolean hasTask(){
        return !(this.task==null);
    }
}
