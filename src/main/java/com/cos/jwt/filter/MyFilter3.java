package com.cos.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        // 토큰: cos 이걸 만들어줘야함. id,pw 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답을 해준다.
        // 요청할 때 마다 header에 Authorization에 value값으로 토큰을 가지고온다.
        // 그 때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증만 하면 됨.( RSA,HS256 )
        if (req.getMethod().equals("POST")){
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println("headerAuth = " + headerAuth);
            System.out.println("필터3");

            if (headerAuth.equals("cos")) {
                filterChain.doFilter(req,res);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
            }
        }

        //filterChain.doFilter(servletRequest,servletResponse);
        // 다시 체인에 넘겨주지않으면 프로세스가 끝나므로 계속 프로세스를 진행하기 위해 넘겨준다.
    }
}
