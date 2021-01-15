package com.codesun.HandlerFilter;

import com.codesun.Filter.RequestFilter;
import com.codesun.Tasks.Task;
import org.springframework.stereotype.Component;


public class MyFilter implements RequestFilter {
    @Override
    public String doFilter(Task task) {
        return task.getJsonText()+"appendOK";
    }
}
