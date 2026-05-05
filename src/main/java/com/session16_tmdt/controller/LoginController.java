package com.session16_tmdt.controller;

import com.session16_tmdt.dto.LoginFormDTO;
import com.session16_tmdt.model.User;
import com.session16_tmdt.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser != null) {
            if ("ADMIN".equals(sessionUser.getRole())) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/";
        }
        model.addAttribute("loginForm", new LoginFormDTO());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginFormDTO loginForm, BindingResult result, HttpSession session,
                        RedirectAttributes ra, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("loginForm", loginForm);
            return "login";
        }

        User user = userService.findByUsername(loginForm.getUsername());

        if (user == null || !user.getPassword().equals(loginForm.getPassword())) {
            ra.addFlashAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
            return "redirect:/auth/login";
        }

        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());

        if ("ADMIN".equals(user.getRole())) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes ra) {
        session.invalidate();
        ra.addFlashAttribute("message", "Đã đăng xuất thành công");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser != null) {
            if ("ADMIN".equals(sessionUser.getRole())) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/";
        }
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid User user, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "register";
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            ra.addFlashAttribute("error", "Tên đăng nhập đã được sử dụng");
            return "redirect:/auth/register";
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            ra.addFlashAttribute("error", "Email đã được đăng ký");
            return "redirect:/auth/register";
        }

        user.setUsername(user.getUsername().trim());
        user.setEmail(user.getEmail().trim());
        user.setRole("CUSTOMER");
        userService.save(user);
        ra.addFlashAttribute("message", "Đăng ký thành công. Vui lòng đăng nhập.");
        return "redirect:/auth/login";
    }
}
