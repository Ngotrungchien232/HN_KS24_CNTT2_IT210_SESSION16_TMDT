package com.session16_tmdt.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        String role = session != null ? (String) session.getAttribute("role") : null;
        String ctx = request.getContextPath();

        if (!"ADMIN".equals(role)) {
            if (role == null) {
                response.sendRedirect(ctx + "/auth/login");
            } else {
                response.sendRedirect(ctx + "/");
            }
            return false;
        }
        return true;
    }
}
