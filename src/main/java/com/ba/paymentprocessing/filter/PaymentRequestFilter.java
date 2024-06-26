package com.ba.paymentprocessing.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class PaymentRequestFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(PaymentRequestFilter.class);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        String clientIp = httpRequest.getRemoteAddr();
        String method = httpRequest.getMethod();
        String requestURI = httpRequest.getRequestURI();

        logger.info("Client IP: {}, Method: {}, URI: {}", clientIp, method, requestURI);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
