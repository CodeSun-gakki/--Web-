package com.codesun.Tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//这里放请求的参数
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterceptTask {
   private String imsi;
   private String phone;
   private String phone_nation_code;
}
