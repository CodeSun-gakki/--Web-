package com.codesun.Filter;

import com.codesun.Tasks.Task;

public interface RequestFilter {
    String doFilter(Task task);
}
