package com.codesun.FilterChain;

import com.codesun.Filter.RequestFilter;
import com.codesun.Tasks.Task;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Component
public class RequestFilterChain implements ApplicationContextAware {
    private List<RequestFilter> filters;
    private ApplicationContext applicationContext;
    int pos=0;
    public RequestFilterChain(){
        filters=new LinkedList<>();
    }
    public void addFilter(RequestFilter filter){
        filters.add(filter);
    }

    @PostConstruct
    public void initFilters(){
       //把这里设置成初始化函数，将上下文全部的RequestFilterChain尽心容器的注入
        String[] names = this.applicationContext.getBeanNamesForType(RequestFilter.class);
        for(String BeanName:names){
            RequestFilter filter = this.applicationContext.getBean(BeanName, RequestFilter.class);
            filters.add(filter);
        }
    }
    public void flip(){
        pos=0;
    }
    public String doFilter(Task task){
        while(pos<filters.size()){
             String object=filters.get(pos).doFilter(task);
             if(object!=null) return object;
             pos++;
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
