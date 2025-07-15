package com.southarmsite.backend;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
public class ApiKeyAuthFilter implements Filter {

    @Value("${security.api.key}")
    private String configuredApiKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String method = httpRequest.getMethod();

        // Skip authentication for GET requests
        if ("GET".equals(method)) {
            chain.doFilter(request, response);
            return;
        }

        // Check API key for all other methods (POST, PUT, DELETE, PATCH, etc.)
        String apiKey = httpRequest.getHeader("X-API-Key");

        if (configuredApiKey.equals(apiKey)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\":\"Unauthorized\"}");
        }
    }
}