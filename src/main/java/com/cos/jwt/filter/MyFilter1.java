package com.cos.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        System.out.println("필터1");
        filterChain.doFilter(servletRequest,servletResponse);
        // 다시 체인에 넘겨주지않으면 프로세스가 끝나므로 계속 프로세스를 진행하기 위해 넘겨준다.
    }
}
