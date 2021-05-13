package com.codesun.Tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//这里放请求的参数
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InterceptTask {
   private String id;
   private String imsi;
   private String phone;
   private String phone_nation_code;

   public boolean hasMessage(){
      return this.id!=null&&this.imsi!=null&&this.phone!=null&&this.phone_nation_code!=null;
   }
}
