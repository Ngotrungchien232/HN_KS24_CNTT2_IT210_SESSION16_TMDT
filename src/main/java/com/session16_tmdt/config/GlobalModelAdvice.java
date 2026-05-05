package com.session16_tmdt.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("currentUsername")
    public String currentUsername(HttpSession session) {
        Object u = session.getAttribute("username");
        return u != null ? u.toString() : null;
    }

    @ModelAttribute("currentRole")
    public String currentRole(HttpSession session) {
        Object r = session.getAttribute("role");
        return r != null ? r.toString() : null;
    }
}
