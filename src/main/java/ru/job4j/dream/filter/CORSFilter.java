package ru.job4j.dream.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest HttpRequest = (HttpServletRequest) request;
        HttpServletResponse HttpResponse = (HttpServletResponse) response;

        HttpResponse.addHeader("Access-Control-Allow-Origin", "*");
        HttpResponse.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");

        if (HttpRequest.getMethod().equals("OPTIONS")) {
            HttpResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }

        chain.doFilter(HttpRequest, response);
    }
}
